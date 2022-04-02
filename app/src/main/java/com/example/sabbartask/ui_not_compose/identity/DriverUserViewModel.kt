package com.example.sabbartask.ui_not_compose.identity


import com.example.sabbartask.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DriverUserViewModel  @Inject constructor(
    private val userRepository: DriverUserRepository
    ) : BaseViewModel(userRepository) {

}