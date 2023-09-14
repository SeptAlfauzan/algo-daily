package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.datastore.SortBy
import com.septalfauzan.algotrack.data.datastore.SortType
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.domain.model.ui.UiState
import com.septalfauzan.algotrack.domain.usecase.IAttendanceHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryAttendanceViewModel @Inject constructor(private val historyAttendanceUseCase: IAttendanceHistoryUseCase) : ViewModel() {
    private val _result = MutableStateFlow<UiState<List<AttendanceEntity>>>(UiState.Loading)
    val result: StateFlow<UiState<List<AttendanceEntity>>> = _result

    private val _detail = MutableStateFlow<UiState<AttendanceEntity>>(UiState.Loading)
    val detail: StateFlow<UiState<AttendanceEntity>> = _detail

    private val _timestampSortType = MutableStateFlow<SortType>(SortType.ASC)
    private val _statusSortType = MutableStateFlow<SortType>(SortType.ASC)

    val timestampSortType: StateFlow<SortType> = _timestampSortType
    val statusSortType: StateFlow<SortType> = _statusSortType

    init {
        getSortType()
    }

    fun getHistory(date: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historyAttendanceUseCase.getHistory(date).catch { e->
                    _result.value = UiState.Error(e.message.toString())
                }.collect{data ->
                    _result.value = UiState.Success(data)
                }
            }catch (e: Exception){
                _result.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun reloadHistory(){
        viewModelScope.launch(Dispatchers.IO) { _result.value = UiState.Loading }
    }

    fun getDetail(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historyAttendanceUseCase.getDetail(id).catch { e ->
                    _detail.value = UiState.Error(e.message.toString())
                }.collect{   data ->
                    _detail.value = UiState.Success(data)
                }
            } catch (e: Exception){
                _detail.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun sortBy(column: SortBy, sortType: SortType){
        viewModelScope.launch(Dispatchers.IO) {
            historyAttendanceUseCase.setSortByAndType(column, sortType)
            getSortType()
            reloadHistory()
        }
    }

    private fun getSortType(){
        viewModelScope.launch(Dispatchers.IO) {
            historyAttendanceUseCase.getSortByStatusValue().collect{ _statusSortType.value = it }
            historyAttendanceUseCase.getSortByTimestampValue().collect{ _timestampSortType.value = it }
        }
    }

    fun reloadDetail(){
        viewModelScope.launch(Dispatchers.IO) { _detail.value = UiState.Loading }
    }
}