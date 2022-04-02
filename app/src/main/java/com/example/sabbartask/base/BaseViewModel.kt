package com.example.sabbartask.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sabbartask.R
import com.example.sabbartask.data.remote.entities.BaseResponse
import com.example.sabbartask.utils.ERRORS
import com.example.sabbartask.utils.SingleLiveEvent
import com.example.sabbartask.utils.Status
import com.example.sabbartask.utils.extensions.toObjectFromJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


abstract class BaseViewModel (private val repository: BaseRepository) : ViewModel() {
    val showNetworkError = SingleLiveEvent<Boolean>()

    fun <D> performNetworkCall(
        apiCall: suspend () -> Response<D>,
        status: SingleLiveEvent<Status>,
        doOnSuccess: (responseData: D?) -> Unit = {},
        isDatabase: Boolean? = false
    ) {

        if (isDatabase == true) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val response = apiCall.invoke()
                    status.postValue(Status.Success(response.body()))
                }
            }
        } else {
        if (isNetworkConnected()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        status.postValue(Status.Loading)
                        val response = apiCall.invoke()
                        when {
                            response.code() == 206 -> {
                                status.postValue(Status.Error(206, data = response.body()))
                            }
                            response.code() in 200..300 -> {
                                doOnSuccess(response.body())
                                status.postValue(Status.Success(response.body()))
                            }
                            response.code() == 401 -> {
                                status.postValue(
                                    Status.Error(
                                        errorCode = ERRORS.UN_AUTHORIZED
                                    )
                                )
                            }
                            else -> {
                                val error =
                                    response.errorBody()?.string()
                                        .toObjectFromJson<BaseResponse<Any?>>(BaseResponse::class.java)
                                status.postValue(
                                    Status.Error(
                                        response.code(),
                                        errorCode = ERRORS.UN_EXPECTED,
                                        message = if (error.message.isNullOrEmpty()) repository.getString(
                                            R.string.error_unknown
                                        ) else error.message,
                                        errors = error.errors
                                    )
                                )
                            }
                        }
                    } catch (e: Exception) {
                        status.postValue(
                            Status.Error(
                                errorCode = ERRORS.UNKNOWN,
                                message = repository.getString(R.string.error_unknown)
                            )
                        )
                    }
                }
            }
        } else
            status.postValue(
                Status.Error(
                    errorCode = ERRORS.NO_INTRERNET,
                    message = repository.getString(R.string.check_internet_connection)
                )
            )
    }
    }

    fun doInBackground(error: (e: Exception) -> Unit = {}, block: suspend () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    block.invoke()
                } catch (e: Exception) {
                    error.invoke(e)
                }
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        val isNetworkConnected = repository.isNetworkConnected()
        if (!isNetworkConnected)
            this.showNetworkError.postValue(true)
        return isNetworkConnected
    }


}