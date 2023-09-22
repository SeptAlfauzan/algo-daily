package com.septalfauzan.algotrack.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.WorkManager
import com.septalfauzan.algotrack.util.Notification
import com.septalfauzan.algotrack.util.REMINDER_WORK_MANAGER_TAG

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Start your application here
            context?.let{
                val notificationHelper = Notification.getInstance(context)
                notificationHelper.setDailyReminder()
                Log.d("TAG", "notification alarm manager starting... ")
            }
        }
    }
}