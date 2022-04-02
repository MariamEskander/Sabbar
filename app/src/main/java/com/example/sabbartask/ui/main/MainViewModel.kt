package com.example.sabbartask.ui.main


import com.example.sabbartask.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val repository: MainRepository
    ) : BaseViewModel(repository) {


}