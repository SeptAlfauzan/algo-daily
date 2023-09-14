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
        val outputFormat = SimpleDateFormat("EEEE, hh:mm a", Locale.getDefault())
        val data = inputFormat.parse(this)

        outputFormat.format(data)
    }catch (e: java.lang.Exception){
        e.printStackTrace()
        this
    }
}

fun String.formatToLocaleGMT() : String{
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val data = inputFormat.parse(this)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        outputFormat.format(data)
    }catch (e: java.lang.Exception){
        e.printStackTrace()
        this
    }
}


fun String.formatGMTtoUTC() : String{
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        val data = inputFormat.parse(this)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        outputFormat.format(data)
    }catch (e: java.lang.Exception){
        e.printStackTrace()
        this
    }
}



fun String.formatTimeStampDatasourceHourMinute(): String{
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
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

fun String.formatDatePendingEntity(): String {
    val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Locale.US)
    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    outputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = inputDateFormat.parse(this)
    return outputDateFormat.format(date)
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

