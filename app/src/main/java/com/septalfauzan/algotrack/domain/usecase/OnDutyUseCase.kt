package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.repository.OnDutyStatusRepository
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import com.septalfauzan.algotrack.domain.repository.IOnDutyStatusRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnDutyUseCase @Inject constructor(private val onDutyStatusRepository: IOnDutyStatusRepository) : IOnDutyUseCase {
    override suspend fun getOnDutyStatus(): Flow<Boolean> = onDutyStatusRepository.getOnDutyStatus()

    override suspend fun setOnDutyStatus(status: Boolean) {
        onDutyStatusRepository.setOnDutyStatus(status)
    }
}