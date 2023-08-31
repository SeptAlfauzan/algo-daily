package com.septalfauzan.algotrack.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.septalfauzan.algotrack.domain.usecase.IPendingAttendanceUseCase
import com.septalfauzan.algotrack.service.DailyAttendanceWorker
import javax.inject.Inject

class DailyAttendanceWorkerFactory @Inject constructor(
    private val pendingAttendanceUseCase: IPendingAttendanceUseCase
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            DailyAttendanceWorker::class.java.name ->
                DailyAttendanceWorker( appContext, workerParameters, pendingAttendanceUseCase)
            else ->
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
        }
    }
}