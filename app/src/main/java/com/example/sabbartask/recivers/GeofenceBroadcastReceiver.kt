package com.example.sabbartask.recivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.sabbartask.R
import com.example.sabbartask.data.remote.entities.NOTIFCATION_TYPE
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == ACTION_GEOFENCE_EVENT) {
//            val geofencingEvent = GeofencingEvent.fromIntent(intent)
//
//            if (geofencingEvent.hasError()) {
//                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
//                Log.e(TAG, errorMessage)
//                return
//            }
//
//            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
//                Log.v(TAG, context.getString(R.string.geofence_entered))
//
//                val fenceId = when {
//                    geofencingEvent.triggeringGeofences.isNotEmpty() ->
//                        geofencingEvent.triggeringGeofences[0].requestId
//                    else -> {
//                        Log.e(TAG, "No Geofence Trigger Found! Abort mission!")
//                        return
//                    }
//                }
//                // Check geofence against the constants listed in GeofenceUtil.kt to see if the
//                // user has entered any of the locations we track for geofences.
//                val driverLocation = when(fenceId){
//                    NOTIFCATION_TYPE.NEAR_PICKUP.name -> {
//                        "near pickup location"
//                    }
//                    NOTIFCATION_TYPE.PICKUP.name -> {
//                        "in pickup location"
//                    }
//                    NOTIFCATION_TYPE.NEAR_DROP_OFF.name -> {
//                        "near drop-off location"
//                    }
//                    NOTIFCATION_TYPE.DROP_OFF.name -> {
//                        "in drop-off location"
//                    }
//                    else -> ""
//                }
//
//                // Unknown Geofences aren't helpful to us
//                if ( driverLocation.isEmpty() ) {
//                    Log.e(TAG, "Unknown Geofence: Abort Mission")
//                    return
//                }
//
//                val notificationManager = ContextCompat.getSystemService(
//                    context,
//                    NotificationManager::class.java
//                ) as NotificationManager
//
//                notificationManager.sendGeofenceEnteredNotification(
//                    context, driverLocation
//                )
//            }
//        }
    }
}

private const val TAG = "GeofenceReceiver"
