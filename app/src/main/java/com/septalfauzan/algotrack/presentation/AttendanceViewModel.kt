package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.data.repository.AttendanceRepository
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.domain.model.Attendance
import com.septalfauzan.algotrack.domain.model.ui.AttendanceFormUiState
import com.septalfauzan.algotrack.domain.usecase.IOnDutyUseCase
import com.septalfauzan.algotrack.helper.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repository: AttendanceRepository,
    private val onDutyUseCase: IOnDutyUseCase
) : ViewModel() {
    private var _onDutyStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onDutyStatus: StateFlow<Boolean> = _onDutyStatus

    private var _attendanceFormState: MutableStateFlow<AttendanceFormUiState> = MutableStateFlow(
        AttendanceFormUiState()
    )
    val attendanceFormUiState: StateFlow<AttendanceFormUiState> = _attendanceFormState

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        getOnDutyStatusValue()
    }
    /**
     * @see This method is used to create new attendance and delete pending attendance (local database)
     */
    fun createAttendance(
        id: String,
        createdAt: String,
        selectedAnswer: String,
        reasonNotWork: String,
        latitude: Double,
        longitude: Double,
        navController: NavController
    ) {
        val status =
            if (selectedAnswer == "Yes") AttendanceStatus.ON_DUTY else AttendanceStatus.PERMIT
        val attendanceEntity = Attendance(
            status = status,
            reason = if (selectedAnswer == "No") reasonNotWork else null,
            latitude = latitude,
            longitude = longitude,
            created_at = createdAt
        )

        viewModelScope.launch {
            _attendanceFormState.value = _attendanceFormState.value.copy(
                onLoading = true
            )
            try {
                repository.createAttendance(id, attendanceEntity)
                withContext(Dispatchers.Main) {
                    navController.navigate(
                        Screen.Success.createRoute(
                            R.string.absence_sent_successfully.toString(),
                            R.string.thank_you_message.toString()
                        )
                    ) {
                        popUpTo(Screen.Attendance.route) {
                            inclusive = true
                        }
                    }
                }
            } catch (e: Exception) {
                eventChannel.send(MyEvent.MessageEvent("Error: ${e.message}"))
            } finally {
                _attendanceFormState.value = _attendanceFormState.value.copy(
                    onLoading = false
                )
            }
        }
    }

    fun setOnDutyValue(status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            onDutyUseCase.setOnDutyStatus(status)
            getOnDutyStatusValue()
        }
    }

    private fun getOnDutyStatusValue() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onDutyUseCase.getOnDutyStatus().catch { e ->
                    e.printStackTrace()
                }.collect {
                    _onDutyStatus.value = it
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}