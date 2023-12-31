package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.data.datastore.SortBy
import com.septalfauzan.algotrack.data.datastore.SortType
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.model.Attendance
import com.septalfauzan.algotrack.data.source.remote.apiResponse.AttendanceResponse
import kotlinx.coroutines.flow.Flow

interface IAttendanceRepository {
    suspend fun getHistory(date: String) : Flow<List<AttendanceEntity>>
    suspend fun getDetail(id: String) : Flow<AttendanceEntity>
    suspend fun updateAttendance(id: String, data: Attendance) : Flow<AttendanceResponse>
    suspend fun createAttendance(pendingId: String, data: Attendance) : Flow<AttendanceResponse>
    suspend fun saveToLocalDB(data: AttendanceEntity) : Flow<Long>
    suspend fun saveBatchToLocalDB(listData: List<AttendanceEntity>) : Flow<List<Long>>
    suspend fun createNewBlankAttendance() : Flow<PendingAttendanceEntity>
    suspend fun setSortByAndType(column: SortBy, sortType: SortType)

    suspend fun getSortByTimestampValue() : Flow<SortType>
    suspend fun getSortByStatusValue() : Flow<SortType>
    suspend fun deleteLocalPendingAttendance(pendingAttendanceEntity: PendingAttendanceEntity): Long
    suspend fun deleteLocalAttendanceHistoryRecord() : Flow<Int>
    suspend fun deleteLocalAttendanceData(): Unit
}