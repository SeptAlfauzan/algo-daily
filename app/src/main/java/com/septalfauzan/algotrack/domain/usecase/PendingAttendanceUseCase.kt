package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.data.source.remote.apiResponse.AttendanceResponse
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class PendingAttendanceUseCase @Inject constructor(private val attendanceRepository: IAttendanceRepository) : IPendingAttendanceUseCase {
//    override suspend fun create(): StateFlow<AttendanceResponse?> {
//        val result: MutableStateFlow<AttendanceResponse?> = MutableStateFlow(null)
//        try {
//            val response = attendanceRepository.createNewBlankAttendance()
//            response.catch { error ->
//                throw error
//            }.collect{
//                result.value = it
//            }
//        }catch (e: Exception){
//            throw e
//        }
//        return result
//    }


    override suspend fun create(): StateFlow<PendingAttendanceEntity?> {
        val result: MutableStateFlow<PendingAttendanceEntity?> = MutableStateFlow(null)
        try {
            val response = attendanceRepository.createNewBlankAttendance()
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