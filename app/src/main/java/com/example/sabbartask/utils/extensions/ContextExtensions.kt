package com.example.sabbartask.utils.extensions

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.sabbartask.utils.Constants


fun Context.getColorCompat(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getDrawableCompat(@DrawableRes drawable: Int): Drawable {
    return ContextCompat.getDrawable(this, drawable)!!
}

fun Context.getSharedPref(): SharedPreferences {
    return this.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
}

