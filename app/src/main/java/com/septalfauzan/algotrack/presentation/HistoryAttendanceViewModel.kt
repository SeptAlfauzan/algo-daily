package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceHistoryResponse
import com.septalfauzan.algotrack.domain.usecase.HistoryAttendanceUseCase
import com.septalfauzan.algotrack.domain.usecase.IHistoryAttendanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HistoryAttendanceViewModel @Inject constructor(private val historyAttendanceUseCase: IHistoryAttendanceUseCase) : ViewModel() {
    private val _result = MutableStateFlow<UiState<List<AttendanceEntity>>>(UiState.Loading)
    val result: StateFlow<UiState<List<AttendanceEntity>>> = _result

    private val _detail = MutableStateFlow<UiState<AttendanceEntity>>(UiState.Loading)
    val detail: StateFlow<UiState<AttendanceEntity>> = _detail


    fun getHistory(date: String){
        viewModelScope.launch(Dispatchers.IO) {
            _result.value = historyAttendanceUseCase.getHistory(date).value
        }
    }

    fun reloadHistory(){
        viewModelScope.launch(Dispatchers.IO) { _result.value = UiState.Loading }
    }

    fun getDetail(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            _detail.value = historyAttendanceUseCase.getDetail(id).value
        }
    }

    fun reloadDetail(){
        viewModelScope.launch(Dispatchers.IO) { _detail.value = UiState.Loading }
    }
}