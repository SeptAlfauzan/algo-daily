package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.datastore.SortBy
import com.septalfauzan.algotrack.data.datastore.SortType
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.ui.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

interface IHistoryAttendanceUseCase {
    suspend fun getHistory(date: String) : Flow<List<AttendanceEntity>>
    suspend fun getDetail(id: String) : Flow<AttendanceEntity>
    suspend fun setSortByAndType(column: SortBy, sortType: SortType)
    suspend fun getSortByTimestampValue() : Flow<SortType>
    suspend fun getSortByStatusValue() : Flow<SortType>
}