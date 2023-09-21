package com.septalfauzan.algotrack.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.septalfauzan.algotrack.service.AttendanceReminder

class  Notification(private val context: Context) {
    fun setDailyReminder() {
//        set alarm for attendance every hour from 8am to 4pm
        val workHour = (8..16).map {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, it)
            calendar.set(Calendar.MINUTE, 0)
            calendar
        }
        workHour.mapIndexed { index, hour ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 21)
            calendar.set(Calendar.MINUTE, 6+(index*10))

            val intent = Intent(context, AttendanceReminder::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context, ID_REPEATING+index, intent, PendingIntent.FLAG_IMMUTABLE)
            val alarmManager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 21)
//        calendar.set(Calendar.MINUTE, 17)
//
//        val intent = Intent(context, AttendanceReminder::class.java)
//        val pendingIntent =
//            PendingIntent.getBroadcast(context, ID_REPEATING+3012, intent, PendingIntent.FLAG_IMMUTABLE)
//        val alarmManager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//            pendingIntent
//        )
    }

    fun cancelAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AttendanceReminder::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    companion object {
        @Volatile
        private var INSTANCE: Notification? = null

        fun getInstance(context: Context): Notification {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Notification(context)
                }
                return INSTANCE as Notification
            }
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
}
