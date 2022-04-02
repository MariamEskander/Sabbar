package com.example.sabbartask.services

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.sabbartask.data.remote.entities.FirebaseData
import com.example.sabbartask.data.remote.entities.LocationData
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*

class LocationService : Service(), OnGetLocation, LocationListener {
    private val TAG = "LocationService"

    var repeatTask = Timer()
    var repeatInterval = 30000L

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase?.getReference("Data")

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(FirebaseData::class.java)
                repeatTask.scheduleAtFixedRate(
                    object : TimerTask() {
                        override fun run() {
                            if (value?.pickup?.lat == 0.0
                                && value.pickup?.lng == 0.0 &&
                                value.dropoff?.lat == 0.0
                                && value.dropoff?.lng == 0.0
                            ) {
                                repeatTask.cancel()
                                this@LocationService.stopSelf()
                                this@LocationService.stopForeground(true)
                            } else
                                LocationUtils.getUserLocation(
                                    this@LocationService,
                                    this@LocationService,
                                    this@LocationService
                                )
                        }

                    }, 0, repeatInterval
                )
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })



        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        var mLocation: Location? = null
    }

    override fun onGetLocation() {
        if (mLocation == null) mLocation = getCurrentLocation()
        mLocation?.let {
            runBlocking {
                coroutineScope {
                    withContext(Dispatchers.IO) {
                        databaseReference?.child("driver")?.setValue(
                            LocationData(it.latitude, it.longitude)
                        )

                    }
                }
            }
        }
    }

    private fun getCurrentLocation(): Location? {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this, Manifest.permission
                    .ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationIfAny: Location? =
                if (locationManager.allProviders.contains(LocationManager.GPS_PROVIDER)) {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
            Log.i(
                "getCurrentLocation",
                locationIfAny?.latitude.toString() + " " + locationIfAny?.longitude
            )
            return locationIfAny
        } else {
            return null
        }

    }

    override fun onLocationChanged(p0: Location) {
        Log.e(TAG, "onLocationChanged fn: $p0")
        mLocation = p0
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

}