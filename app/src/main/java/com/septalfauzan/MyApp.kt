package com.septalfauzan

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.septalfauzan.algotrack.di.DailyAttendanceWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {
    @Inject
    lateinit var hiltWorkerFactory: DailyAttendanceWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
    }

}