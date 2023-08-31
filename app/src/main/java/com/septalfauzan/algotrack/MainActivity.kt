package com.septalfauzan.algotrack

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.WorkManager
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.presentation.*
import com.septalfauzan.algotrack.service.DailyAttendanceWorker
import com.septalfauzan.algotrack.util.REMINDER_WORK_MANAGER_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel: AuthViewModel by viewModels()
        val registerViewModel: RegisterViewModel by viewModels()
        val timerViewModel: TimerViewModel by viewModels()
        val notificationViewModel: NotificationViewModel by viewModels()
        val themeViewModel: ThemeViewModel by viewModels()
        val profileViewModel: ProfileViewModel by viewModels()
        val historyAttendanceViewModel: HistoryAttendanceViewModel by viewModels()
        val attendanceViewModel: AttendanceViewModel by viewModels()

        installSplashScreen().setKeepOnScreenCondition {//splash screen will disapprear whenever already check auth token
            authViewModel.isLoadingSplash.value
        }

        setContent {
            AlgoTrackTheme(darkTheme = themeViewModel.isDarkTheme.collectAsState().value) {
                val isLogged by authViewModel.isLogged.collectAsState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AlgoDailyApp(
                        isLogged = isLogged,
                        authViewModel = authViewModel,
                        registerViewModel = registerViewModel,
                        timerViewModel = timerViewModel,
                        themeViewModel = themeViewModel,
                        notificationViewModel = notificationViewModel,
                        profileViewModel = profileViewModel,
                        historyAttendanceViewModel = historyAttendanceViewModel,
                        attendanceViewModel = attendanceViewModel,
                    )
                }
            }
        }
    }
}