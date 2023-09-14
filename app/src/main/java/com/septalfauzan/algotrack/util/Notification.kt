package com.septalfauzan.algotrack.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import com.septalfauzan.algotrack.service.AttendanceReminder

object Notification {
    fun setDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AttendanceReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)

        val workDays = listOf(
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
        )

//        workDays.map { day ->
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.HOUR_OF_DAY, 16)
//            calendar.set(Calendar.MINUTE, 40)
//            calendar.set(Calendar.DAY_OF_WEEK, day)
//
//
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 10000, pendingIntent)
//        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 23)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AttendanceReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
