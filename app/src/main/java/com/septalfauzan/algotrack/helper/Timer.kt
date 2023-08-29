package com.septalfauzan.algotrack.helper

import android.content.Context
import com.septalfauzan.algotrack.R
import java.text.SimpleDateFormat
import java.util.*

fun Long.formatMilliseconds(): String{
    val format = SimpleDateFormat("mm:ss")
    return format.format(Date(this))
}

fun Context.getCurrentDayCycle():String{
    val format = SimpleDateFormat("HH", Locale.US)
    val hour = format.format(Date())

    val calendar = Calendar.getInstance()
    val hourDay = calendar.get(Calendar.HOUR_OF_DAY)

    return when(hourDay){
        in 1..12 -> this.getString(R.string.morning)
        in 12..15 -> getString(R.string.afternoon)
        in 15..19 -> getString(R.string.evening)
        else -> getString(R.string.night)
    }
}