package com.example.sabbartask.data.local.sharedPref

import android.content.Context
import com.example.sabbartask.utils.extensions.getSharedPref


class SharedPreferencesUtils private constructor(private val prefHelper: PrefHelper) {

//    fun isLoggedInUser(): Boolean {
//        return getUser() != null
//    }
//
//    fun saveUser(user: UserResponse?) {
//        prefHelper.putObject(USER, user)
//    }
//
//    fun getUser(): UserResponse? {
//        return prefHelper.getObject<UserResponse>(
//            USER,
//            UserResponse::class.java
//        )
//    }

//    fun clearUser() {
//        saveUser(null)
//    }


   fun isFirstTimeOpenApp() : Boolean{
       return prefHelper.getBoolean(IS_FIRST_OPEN,true)
   }

    fun setFirstTimeOpenApp(){
        return prefHelper.putBoolean(IS_FIRST_OPEN,false)
    }


    companion object {
        const val USER = "user"
        const val IS_FIRST_OPEN = "is-first-time-open"

        private var sharedPreferencesUtils: SharedPreferencesUtils? = null

        fun getInstance(context: Context): SharedPreferencesUtils {
            if (sharedPreferencesUtils == null)
                sharedPreferencesUtils = SharedPreferencesUtils(PrefHelper(context.getSharedPref()))

            return sharedPreferencesUtils!!
        }
    }
}