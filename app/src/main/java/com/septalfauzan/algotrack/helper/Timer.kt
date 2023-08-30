package com.septalfauzan.algotrack.helper

import android.content.Context
import com.septalfauzan.algotrack.R
import java.text.SimpleDateFormat
import java.util.*

fun Long.formatMilliseconds(): String{
    val format = SimpleDateFormat("mm:ss")
    return format.format(Date(this))
}

fun Calendar.getMilliSecFromMinutesSecond(): Long{
    val currentMinute = this.get(Calendar.MINUTE)
    val currentSecond = this.get(Calendar.SECOND)
    return currentMinute * 60 * 1000L + currentSecond * 1000L
}

fun String.formatTimeStampDatasource(): String{
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("EEEE, hh:mm a, dd MMM yyyy", Locale.getDefault())
        val data = inputFormat.parse(this)

        outputFormat.format(data)
    }catch (e: java.lang.Exception){
        e.printStackTrace()
        this
    }
}

fun String.formatCalendarDate(): String{
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val outputFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        val data = inputFormat.parse(this)

        outputFormat.format(data)
    }catch (e: java.lang.Exception){
        e.printStackTrace()
        this
    }
}

fun String.reverseFormatCalendarDate(): String{
    return try {
        val inputFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.US)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val data = inputFormat.parse(this)

        outputFormat.format(data)
    }catch (e: java.lang.Exception){
        e.printStackTrace()
        this
    }
}

fun Context.getCurrentDayCycle():String{
    val calendar = Calendar.getInstance()
    return when(calendar.get(Calendar.HOUR_OF_DAY)){
        in 1..12 -> this.getString(R.string.morning)
        in 12..15 -> getString(R.string.afternoon)
        in 15..19 -> getString(R.string.evening)
        else -> getString(R.string.night)
    }
}