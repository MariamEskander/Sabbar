package com.example.sabbartask.utils


sealed class Status {
    data class Success<T>(
        var data: T?
    ) : Status()

    object SuccessLoadingMore : Status()

    data class Error(
        var code: Int? = 0,
        var message: String? = "",
        var messageResourceId : Int? = null,
        var errors: Map<String, List<String>>? = null,
        var errorCode : ERRORS? = null,
        var data : Any? = null
    ) : Status()

    data class ErrorLoadingMore(var code: Int = 0, var message: String?) : Status()

    object Loading : Status()

    object LoadingMore : Status()

    object IsValidData : Status()
}