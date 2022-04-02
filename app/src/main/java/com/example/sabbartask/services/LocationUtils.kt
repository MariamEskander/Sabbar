package com.example.sabbartask.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat


var LOCATION_REFRESH_TIME = 60000 // 3 seconds. The Minimum Time to get location update
var LOCATION_REFRESH_DISTANCE =
    0 // 0 meters. The Minimum Distance to be changed to get location update

object LocationUtils {

    fun checkLocationPermissions(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && (
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) ||
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

                )
    }

    @SuppressLint("InlinedApi")
    fun requestPermissions(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            requestCode
        )
    }


    fun getUserLocation(
        locationListener: LocationListener,
        context: Context,
        myListener: OnGetLocation
    ) {
        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED) ||
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Handler(Looper.getMainLooper()).post {
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME.toLong(),
                    LOCATION_REFRESH_DISTANCE.toFloat(),
                    locationListener
                )
            }


            myListener.onGetLocation()
        }
    }
}

interface OnGetLocation {
    fun onGetLocation()
}