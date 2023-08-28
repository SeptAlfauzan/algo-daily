package com.septalfauzan.algotrack.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDeepLinkBuilder
import com.septalfauzan.algotrack.MainActivity
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.presentation.AttendanceViewModel
import com.septalfauzan.algotrack.util.NOTIFICATION_CHANNEL_ID
import com.septalfauzan.algotrack.util.NOTIFICATION_CHANNEL_NAME
import com.septalfauzan.algotrack.util.NOTIFICATION_ID
import com.septalfauzan.algotrack.util.executeThread
import dagger.hilt.android.AndroidEntryPoint

class AttendanceReminder : BroadcastReceiver() {
    private fun showNotification(context: Context, id: String) {
        //TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notificationStyle = NotificationCompat.InboxStyle()
        val timeString = "test"
//        content.forEach {
//            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
//            notificationStyle.addLine(courseData)
//        }
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "https://algodaily/detail/$id".toUri(),
            context,
            MainActivity::class.java
        )

        deepLinkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(notificationStyle)
                .setContentTitle("test")
                .setContentText("test content")
                .setContentIntent(deepLinkPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notification.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        executeThread {
            Log.d(AttendanceReminder::class.java.simpleName, "onReceive: reminder")
            context?.let {
                showNotification(context, "dummy_id")
            }
        }
    }
}