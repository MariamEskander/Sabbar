package com.example.sabbartask.ui.features.userDriver

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.sabbartask.ui.home.NavigationKeys
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED


@HiltViewModel
class UserDriverViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val _requestLocationPermission: MutableStateFlow<Boolean> =  MutableStateFlow(false)

    val requestLocationPermission = _requestLocationPermission.asStateFlow()

    var state by mutableStateOf(
        UserDriverContract.State(
            null
        )
    )
        private set

    var data =  Channel<UserDriverContract.Data>(UNLIMITED)
    private set


    init {
        viewModelScope.launch {
            val isUser = stateHandle.get<Boolean>(NavigationKeys.Arg.IDENTITY)
                ?: throw IllegalStateException("No IDENTITY was passed to destination.")
            state = state.copy(isUser = isUser)
        }
    }

    fun setRequestLocationPermission(request: Boolean) {
        _requestLocationPermission.value = request
    }


}
