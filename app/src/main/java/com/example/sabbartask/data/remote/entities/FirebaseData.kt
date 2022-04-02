package com.example.sabbartask.data.remote.entities

import com.google.gson.annotations.SerializedName

data class FirebaseData(
    @SerializedName("pickup") var pickup: LocationData?= null,
    @SerializedName("dropoff") var dropoff: LocationData?= null,
    @SerializedName("driver") var driver: LocationData?= null,
    @SerializedName("notificationType") var notificationType: Int = -1
)

data class LocationData(@SerializedName("lat") var lat: Double?= null,
                        @SerializedName("lng") var lng: Double?= null)

enum class NOTIFCATION_TYPE{
    NEAR_PICKUP,
    PICKUP,
    DROP_OFF,
    NEAR_DROP_OFF,
    RATE
}