package com.example.sabbartask.ui_not_compose.identity


import com.example.sabbartask.base.BaseRepository
import com.example.sabbartask.data.remote.apiCalls.UserApiCalls
import javax.inject.Inject


class DriverUserRepository @Inject constructor(private val apiCalls: UserApiCalls) : BaseRepository() {

}