package com.septalfauzan.algotrack

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
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
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.presentation.*
import com.septalfauzan.algotrack.service.DailyAttendanceWorker
import com.septalfauzan.algotrack.util.Notification
import com.septalfauzan.algotrack.util.REMINDER_WORK_MANAGER_TAG
import com.septalfauzan.algotrack.util.REQUEST_BOOT_PERMISSION
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
        val backgroundServiceViewModel: BackgroundServiceViewModel by viewModels()

        installSplashScreen().setKeepOnScreenCondition {
            authViewModel.isLoadingSplash.value
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

//        val workManager = WorkManager.getInstance(this)
//        workManager.cancelAllWorkByTag(REMINDER_WORK_MANAGER_TAG)
//        workManager.pruneWork()
//        workManager.enqueue(DailyAttendanceWorker.periodicWorkRequest)
        val notificationHelper = Notification.getInstance(this)
        notificationHelper.setDailyReminder()

        val packageName = packageName
        val powerManager: PowerManager = getSystemService(POWER_SERVICE) as PowerManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = android.Manifest.permission.RECEIVE_BOOT_COMPLETED
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), REQUEST_BOOT_PERMISSION)
            }
        }

//        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
//            Toast.makeText(this, "no allowed", Toast.LENGTH_SHORT).show()
//            // it is not enabled. Ask the user to do so from the settings.
//        openBackgroundServiceSetting(this)
//        }else {
//            Toast.makeText(this, "allowed", Toast.LENGTH_SHORT).show();
//            // good news! It works fine even in the background.
//        }

        setContent {
            AlgoTrackTheme(darkTheme = themeViewModel.isDarkTheme.collectAsState().value) {
                val isLogged by authViewModel.isLogged.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    contentColor = if(themeViewModel.isDarkTheme.collectAsState().value) Color.White else Color.Black
                ) {
                    AlgoDailyApp(
                        openNotificationSetting = {openNotificationSettings(it)},
                        openAutoStartSetting = {openBackgroundServiceSetting(it)},
                        isAutoStarteGranted = backgroundServiceViewModel.isAutoStartGranted,
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
        settingActivityLauncher.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openBackgroundServiceSetting(context: Context){
        val backgroundServiceIntent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
        backgroundServiceIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        val expandedSettingText = String.format("Background activity is restricted on this device.\nPlease allow it so we can post an active notification during work sessions.\n\nTo do so, click on the notification to go to\nApp management -> search for %s -> Battery Usage -> enable 'Allow background activity')", getString(R.string.app_name))
        settingActivityLauncher.launch(backgroundServiceIntent)
    }

    private val settingActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("TAG", ":$result ")
    }

}