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
import com.septalfauzan.algotrack.R

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
            if (!Notification.isWorkDay())  return Result.success()
            if (!Notification.isWorkHour())  return Result.success()
            if(dataStorePreference.getAuthToken().first().isEmpty())  return Result.success()

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
            PeriodicWorkRequestBuilder<DailyAttendanceWorker>(1, TimeUnit.HOURS)
                .addTag(REMINDER_WORK_MANAGER_TAG)
                .setInitialDelay(timerLeft, TimeUnit.MILLISECONDS)
                .build()

        val oneTImeWorkRequest =
            OneTimeWorkRequestBuilder<DailyAttendanceWorker>()
                .addTag(REMINDER_WORK_MANAGER_TAG)
                .setConstraints(networkConstraints)
                .build()
    }
}