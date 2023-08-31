package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.model.AttendanceRequestBody
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponseData
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface IAttendanceRepository {
    suspend fun getHistory() : Flow<List<AttendanceEntity>>
    suspend fun getHistory(date: String) : Flow<List<AttendanceEntity>>
    suspend fun getDetail(id: String) : Flow<AttendanceEntity>
    suspend fun updateAttendance(id: String, data: AttendanceRequestBody) : Flow<AttendanceResponse>
    suspend fun saveToLocalDB(data: AttendanceEntity) : Flow<Long>
    suspend fun saveBatchToLocalDB(listData: List<AttendanceEntity>) : Flow<List<Long>>
    suspend fun createNewBlankAttendance(data: PendingAttendanceEntity) : Flow<AttendanceResponse>
}