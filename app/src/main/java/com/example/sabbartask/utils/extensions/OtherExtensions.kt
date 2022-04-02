package com.example.sabbartask.utils.extensions


import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.sabbartask.R
import com.example.sabbartask.data.remote.entities.LocationData
import com.example.sabbartask.recivers.sendGeofenceEnteredNotification
import com.example.sabbartask.services.LocationService
import com.example.sabbartask.ui_not_compose.identity.adapter.Timeline
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import java.lang.reflect.Type


fun Any?.toJsonString(): String = Gson().toJson(this)

fun <T> String?.toObjectFromJson(type: Type): T = Gson().fromJson(this, type)

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L?, body: (T) -> Unit) =
    liveData?.observe(this, Observer(body))

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun View.hideView() {
    this.visibility = View.GONE
}

fun addToFirebase(databaseReference: DatabaseReference?, attribute: String, data: Any) {
    databaseReference?.child(attribute)?.setValue(data)
}

fun bitmapDescriptorFromVector(context: Context,vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap =
            Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

 fun sendNotifications(context: Context,s: String) {
    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager

    notificationManager.sendGeofenceEnteredNotification(
        context, s
    )
}


fun getLocationDistance(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double,
    results: FloatArray
): Float {
    Location.distanceBetween(lat1, lng1, lat2, lng2, results)
    return results[0]
}


 fun changeViewsColors(context: Context, color: Int, img: AppCompatImageView, v: View?, tv: TextView) {
    v?.background = ContextCompat.getDrawable(context, color)
    tv.setTextColor(ContextCompat.getColor(context, color))
    if (color == R.color.black) img.setImageResource(R.drawable.ic_black_circle)
    else img.setImageResource(R.drawable.white)
}

 fun startLocationService(context: Context) {
    val i = Intent(context, LocationService::class.java)
    context.startService(i)
}

fun isLocationAvailable(data: LocationData?) : Boolean{
   return data?.lat != 0.0 && data?.lng != 0.0
}

fun getTimeLinesItems() : ArrayList<Timeline>{
    val list = ArrayList<Timeline>()
    list.add(Timeline("On the way",true))
    list.add(Timeline("Picked up delivery",false))
    list.add(Timeline("Near delivery destination",false))
    list.add(Timeline("Delivered package",false))

    return list
}
