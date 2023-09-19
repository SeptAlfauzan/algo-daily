package com.septalfauzan.algotrack.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.septalfauzan.algotrack.service.AttendanceReminder

object Notification {
    fun setDailyReminder(context: Context) {
        //set alarm for attendance every hour from 8am to 4pm
        val workHour = (8..16).map {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, it)
            calendar.set(Calendar.MINUTE, 0)
            calendar
        }
        workHour.mapIndexed { index, hour ->
            val intent = Intent(context, AttendanceReminder::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context, ID_REPEATING+index, intent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                hour.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AttendanceReminder::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun isWorkDay(): Boolean {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> false
            Calendar.SATURDAY -> false
            else -> true
        }
    }

    fun isWorkHour(): Boolean {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.HOUR_OF_DAY)
        return when {
            dayOfWeek > 16 -> false
            dayOfWeek < 8 -> false
            else -> true
        }
    }
}
