package com.example.sabbartask.base



import com.example.sabbartask.data.local.LocalDataUtils
import com.example.sabbartask.utils.ConnectivityUtils
import javax.inject.Inject


abstract class BaseRepository  {
    @Inject
     lateinit var  connectivityUtils: ConnectivityUtils

    @Inject
    lateinit var localDataUtils: LocalDataUtils


    fun isNetworkConnected(): Boolean {
        return connectivityUtils.isConnected()
    }

    fun getString(id: Int): String {
        return localDataUtils.getString(id)
    }


}