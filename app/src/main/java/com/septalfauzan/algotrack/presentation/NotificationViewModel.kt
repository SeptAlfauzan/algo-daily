package com.septalfauzan.algotrack.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.septalfauzan.algotrack.data.repository.MainRepository
import com.septalfauzan.algotrack.service.DailyAttendanceWorker
import com.septalfauzan.algotrack.util.REMINDER_WORK_MANAGER_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(@ApplicationContext private val context: Context, private val repository: MainRepository) : ViewModel() {

    private val _isNotificationReminderActive = MutableStateFlow(false)
    val isNotificationReminderActive = _isNotificationReminderActive

    fun setNotificationReminder(){
        try {
            _isNotificationReminderActive.value = true
//            Notification.setDailyReminder(context)
        }catch (e: Exception){
            _isNotificationReminderActive.value = false
            e.printStackTrace()
        }
    }

    fun cancelNotificationReminder(){
        try {
            _isNotificationReminderActive.value = false
            WorkManager.getInstance().cancelAllWorkByTag(REMINDER_WORK_MANAGER_TAG)
//            Notification.cancelAlarm(context)
        }catch (e: Exception){
            _isNotificationReminderActive.value = true
            e.printStackTrace()
        }
    }
}