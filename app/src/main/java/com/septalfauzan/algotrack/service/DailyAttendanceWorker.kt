package com.septalfauzan.algotrack.service

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.usecase.IPendingAttendanceUseCase
import com.septalfauzan.algotrack.domain.usecase.PendingAttendanceUseCase
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
) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        pendingAttendanceUseCase.create(dummy)
        AttendanceReminder.showNotification(
            applicationContext,
            "41fb8493-08f4-4b3b-ac5d-f2e510bfe3ac"
        )
        return Result.success()
    }

    companion object {
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<DailyAttendanceWorker>(1, TimeUnit.MINUTES).addTag(
                REMINDER_WORK_MANAGER_TAG
            ).build()
    }
}