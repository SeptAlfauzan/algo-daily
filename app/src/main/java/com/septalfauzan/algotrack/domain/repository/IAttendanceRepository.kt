package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponse
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface IAttendanceRepository {
    suspend fun getHistory() : Flow<List<AttendanceEntity>>
    suspend fun getHistory(date: String) : Flow<List<AttendanceEntity>>
    suspend fun updateAttendance(data: AttendanceEntity) : Flow<AttendanceResponse>
    suspend fun saveToLocalDB(data: AttendanceEntity) : Flow<Long>
    suspend fun saveBatchToLocalDB(listData: List<AttendanceEntity>) : Flow<List<Long>>
}