package com.septalfauzan.algotrack.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.usecase.IPendingAttendanceUseCase
import com.septalfauzan.algotrack.helper.getMilliSecFromMinutesSecond
import com.septalfauzan.algotrack.util.Notification
import com.septalfauzan.algotrack.util.REMINDER_WORK_MANAGER_TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
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
            if (!Notification.isWorkDay()) throw Exception("Today is day off!")
            if (!Notification.isWorkHour()) throw Exception("It is not working time!")
            if(dataStorePreference.getAuthToken().first().isEmpty()) throw Exception("Not login yet!")

            val response = pendingAttendanceUseCase.create()
            val isOnDuty = dataStorePreference.getOnDutyValue().first()

            response.value?.let {
                if (isOnDuty) AttendanceReminder.showNotification(
                    applicationContext,
                    it.id,
                    it.createdAt
                )
                return Result.success()
            }

            return Result.failure()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
    }

    companion object {
        private val networkConstraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        private val totalTimer = 3600000L
        private val currentDate: Calendar = Calendar.getInstance()
        private val currentMinuteSecond = currentDate.getMilliSecFromMinutesSecond()
        val timerLeft = totalTimer - currentMinuteSecond

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<DailyAttendanceWorker>(15, TimeUnit.MINUTES)
                .addTag(REMINDER_WORK_MANAGER_TAG)
                .setInitialDelay(3, TimeUnit.SECONDS)
                .build()

        val oneTImeWorkRequest =
            OneTimeWorkRequestBuilder<DailyAttendanceWorker>()
                .addTag(REMINDER_WORK_MANAGER_TAG)
                .setConstraints(networkConstraints)
                .build()
    }
}