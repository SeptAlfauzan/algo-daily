package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class PendingAttendanceUseCase @Inject constructor(private val attendanceRepository: IAttendanceRepository) : IPendingAttendanceUseCase {
    override suspend fun create(data: PendingAttendanceEntity): StateFlow<Long> {
        val result = MutableStateFlow(0L)
        try {
            val response = attendanceRepository.createNewBlankAttendance(data)
            response.catch { error ->
                throw error
            }.collect{
                result.value = it
            }
        }catch (e: Exception){
            throw e
        }
        return result
    }

}