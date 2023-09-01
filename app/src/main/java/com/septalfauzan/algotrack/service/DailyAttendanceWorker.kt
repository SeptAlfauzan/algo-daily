package com.septalfauzan.algotrack.service

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.usecase.IPendingAttendanceUseCase
import com.septalfauzan.algotrack.util.REMINDER_WORK_MANAGER_TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*
import java.util.concurrent.TimeUnit


val dummy = PendingAttendanceEntity(
    id = UUID.randomUUID().toString(),
    reason = null,
    status = AttendanceStatus.ON_DUTY,
    latitude = 0.0,
    longitude = 0.0,
    timestamp = "2023-08-29T05:55:21.071Z",
    createdAt = "2023-08-29T05:55:21.071Z",
)

@HiltWorker
class DailyAttendanceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val pendingAttendanceUseCase: IPendingAttendanceUseCase,
    private val dataStorePreference: DataStorePreference
) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            val response = pendingAttendanceUseCase.create()
            response.value?.let {
                Log.d("TAG", "doWork: ${it.data}")
                AttendanceReminder.showNotification(
                    applicationContext,
                    it.data.id
                )
                return Result.success()
            }
            return Result.retry()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    companion object {
        private val networkConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<DailyAttendanceWorker>(5, TimeUnit.MINUTES)
                .addTag(REMINDER_WORK_MANAGER_TAG)
                .setConstraints(networkConstraints)
                .build()
    }
}