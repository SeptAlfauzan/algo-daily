package com.septalfauzan.algotrack.presentation

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.util.Log
import androidx.lifecycle.ViewModel
import com.septalfauzan.algotrack.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BackgroundServiceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MainRepository
) : ViewModel() {
    private val _isAutoStartGranted = MutableStateFlow(false)
    private val _isNotificationGranted = MutableStateFlow(false)
    val isAutoStartGranted: StateFlow<Boolean> = _isAutoStartGranted
    val isNotificationGranted: StateFlow<Boolean> = _isNotificationGranted


    init {
//        Log.d("TAG", ": ${getAutoStartGranted()}")
        _isAutoStartGranted.value = getAutoStartGranted()
        _isNotificationGranted.value = getNotificationGranted()
    }

    private fun getNotificationGranted(): Boolean {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        return when {
            !notificationManager.isNotificationPolicyAccessGranted -> true
            else -> false
        }
    }

    private fun getAutoStartGranted(): Boolean {
        val packageName = context.packageName
        val powerManager: PowerManager = context.getSystemService(POWER_SERVICE) as PowerManager
        return when {
            !powerManager.isIgnoringBatteryOptimizations(packageName) -> true
            else -> false
        }
    }
}