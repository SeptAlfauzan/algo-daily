package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class HistoryAttendanceUseCase @Inject constructor(private val attendanceRepository: IAttendanceRepository) : IHistoryAttendanceUseCase  {
    override suspend fun getHistory(date: String): StateFlow<UiState<List<AttendanceEntity>>> {
        val result: MutableStateFlow<UiState<List<AttendanceEntity>>> = MutableStateFlow(UiState.Loading)
        try {
            attendanceRepository.getHistory(date).catch { error ->
                result.value = UiState.Error("Error: ${error.message}")
            }.collect{response ->
                result.value = UiState.Success(response)
            }
        }catch (e: Exception){
            result.value = UiState.Error("Error: ${e.message}")
        }
        return result
    }
}