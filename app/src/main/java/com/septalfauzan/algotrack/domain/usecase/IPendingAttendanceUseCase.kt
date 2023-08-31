package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponse
import kotlinx.coroutines.flow.StateFlow

interface IPendingAttendanceUseCase {
    suspend fun create(data: PendingAttendanceEntity) : StateFlow<AttendanceResponse?>
}