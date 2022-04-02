package com.example.sabbartask.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class ConnectivityUtils constructor(private val context: Context) {

    /**
     * Get the network info
     */
    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * Check if there is any connectivity
     */
    fun isConnected(): Boolean {
        val info = getNetworkInfo(context)

        return info != null && info.isConnected
    }

    /**
     * Check if there is any connectivity to a Wifi network
     */
    fun isConnectedWifi(): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Check if there is any connectivity to a mobile network
     */
    fun isConnectedMobile(): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }


}
