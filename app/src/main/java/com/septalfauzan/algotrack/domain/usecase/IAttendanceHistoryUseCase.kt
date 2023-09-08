package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.datastore.SortBy
import com.septalfauzan.algotrack.data.datastore.SortType
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import kotlinx.coroutines.flow.Flow

interface IAttendanceHistoryUseCase {
    suspend fun getHistory(date: String) : Flow<List<AttendanceEntity>>
    suspend fun getDetail(id: String) : Flow<AttendanceEntity>
    suspend fun setSortByAndType(column: SortBy, sortType: SortType)
    suspend fun getSortByTimestampValue() : Flow<SortType>
    suspend fun getSortByStatusValue() : Flow<SortType>
    suspend fun deleteLocalHistoryAttendances() : Flow<Int>
}