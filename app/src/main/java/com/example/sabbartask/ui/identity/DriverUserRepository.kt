package com.example.sabbartask.ui.identity


import com.example.sabbartask.base.BaseRepository
import com.example.sabbartask.data.remote.apiCalls.UserApiCalls
import javax.inject.Inject


class DriverUserRepository @Inject constructor(private val apiCalls: UserApiCalls) : BaseRepository() {

}