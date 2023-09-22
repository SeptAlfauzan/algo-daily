package com.septalfauzan.algotrack.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import com.septalfauzan.algotrack.service.AttendanceReminder

class  Notification(private val context: Context) {
    fun setDailyReminder() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        Log.d("TAG", "setDailyReminder: $hour")

        calendar.set(Calendar.HOUR_OF_DAY, hour+1)
        calendar.set(Calendar.MINUTE, 0)

        val intent = Intent(context, AttendanceReminder::class.java)
        intent.putExtra("next-hour", hour+1)

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, null), pendingIntent)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent
        )
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
