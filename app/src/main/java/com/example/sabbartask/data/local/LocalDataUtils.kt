package com.example.sabbartask.data.local

import android.content.Context
import com.example.sabbartask.data.local.sharedPref.SharedPreferencesUtils

class LocalDataUtils constructor(private var context: Context) {

    val sharedPreferencesUtils = SharedPreferencesUtils.getInstance(context)

    fun getString(id: Int): String {
        return context.getString(id)
    }

}