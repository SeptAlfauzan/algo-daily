package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.data.datastore.SortBy
import com.septalfauzan.algotrack.data.datastore.SortType
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.domain.model.AttendanceRequestBody
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponse
import kotlinx.coroutines.flow.Flow

interface IAttendanceRepository {
    suspend fun getHistory(date: String) : Flow<List<AttendanceEntity>>
    suspend fun getDetail(id: String) : Flow<AttendanceEntity>
    suspend fun updateAttendance(id: String, data: AttendanceRequestBody) : Flow<AttendanceResponse>
    suspend fun saveToLocalDB(data: AttendanceEntity) : Flow<Long>
    suspend fun saveBatchToLocalDB(listData: List<AttendanceEntity>) : Flow<List<Long>>
    suspend fun createNewBlankAttendance() : Flow<AttendanceResponse>
    suspend fun setSortByAndType(column: SortBy, sortType: SortType)

    suspend fun getSortByTimestampValue() : Flow<SortType>
    suspend fun getSortByStatusValue() : Flow<SortType>
}