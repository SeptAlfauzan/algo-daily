package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.ui.UiState
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

interface IHistoryAttendanceUseCase {
    suspend fun getHistory(date: String) : StateFlow<UiState<List<AttendanceEntity>>>

    suspend fun getDetail(id: String) : StateFlow<UiState<AttendanceEntity>>
}