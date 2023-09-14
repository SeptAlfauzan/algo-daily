package com.septalfauzan.algotrack

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.work.WorkManager
import com.septalfauzan.algotrack.helper.formatDatePendingEntity
import com.septalfauzan.algotrack.helper.formatToLocaleGMT
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.presentation.*
import com.septalfauzan.algotrack.service.DailyAttendanceWorker
import com.septalfauzan.algotrack.util.Notification
import com.septalfauzan.algotrack.util.REMINDER_WORK_MANAGER_TAG
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel: AuthViewModel by viewModels()
        val registerViewModel: RegisterViewModel by viewModels()
        val timerViewModel: TimerViewModel by viewModels()
        val themeViewModel: ThemeViewModel by viewModels()
        val profileViewModel: ProfileViewModel by viewModels()
        val historyAttendanceViewModel: HistoryAttendanceViewModel by viewModels()
        val attendanceViewModel: AttendanceViewModel by viewModels()

        installSplashScreen().setKeepOnScreenCondition {
            authViewModel.isLoadingSplash.value
        }

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        Log.d("TAG", "current day: $dayOfWeek")

        WindowCompat.setDecorFitsSystemWindows(window, false)
//        Notification.setDailyReminder(this)

        val workManager = WorkManager.getInstance(this)
        workManager.cancelAllWorkByTag(REMINDER_WORK_MANAGER_TAG)
        workManager.enqueue(DailyAttendanceWorker.periodicWorkRequest)

//        openNotificationSettings(this)
        setContent {
            AlgoTrackTheme(darkTheme = themeViewModel.isDarkTheme.collectAsState().value) {
                val isLogged by authViewModel.isLogged.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    contentColor = if(themeViewModel.isDarkTheme.collectAsState().value) Color.White else Color.Black
                ) {
                    AlgoDailyApp(
                        isLogged = isLogged,
                        authViewModel = authViewModel,
                        registerViewModel = registerViewModel,
                        timerViewModel = timerViewModel,
                        themeViewModel = themeViewModel,
                        profileViewModel = profileViewModel,
                        historyAttendanceViewModel = historyAttendanceViewModel,
                        attendanceViewModel = attendanceViewModel,
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openNotificationSettings(context: android.content.Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        notificationSettingsLauncher.launch(intent)
    }

    private val notificationSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("TAG", ": $result")
    }

}