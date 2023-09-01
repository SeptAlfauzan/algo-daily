package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.septalfauzan.algotrack.data.repository.AttendanceRepository
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.domain.model.AttendanceRequestBody
import com.septalfauzan.algotrack.domain.model.UserLocation
import com.septalfauzan.algotrack.domain.usecase.IOnDutyUseCase
import com.septalfauzan.algotrack.helper.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository, private val onDutyUseCase: IOnDutyUseCase) : ViewModel() {
    private var userLocation: UserLocation? = null
    private var _onDutyStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onDutyStatus: StateFlow<Boolean> = _onDutyStatus
    init {
        getOnDutyStatusValue()
    }

    fun updateAttendance(
        id: String,
        selectedAnswer: String,
        reasonNotWork: String,
        navController: NavController
    ) {
        val status = if (selectedAnswer == "Yes") AttendanceStatus.ON_DUTY else AttendanceStatus.PERMIT
        val attendanceEntity = AttendanceRequestBody(
            status = status,
            reason = if (selectedAnswer == "No") reasonNotWork else null,
            latitude = userLocation?.latitude,
            longitude = userLocation?.longitude,
        )

        viewModelScope.launch {
            repository.updateAttendance(id, attendanceEntity)
            withContext(Dispatchers.Main){
                navController.navigate(Screen.Success.route){
                    popUpTo(Screen.Attendance.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    fun setOnDutyValue(status: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            onDutyUseCase.setOnDutyStatus(status)
            getOnDutyStatusValue()
        }
    }

    private fun getOnDutyStatusValue(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onDutyUseCase.getOnDutyStatus().catch { e ->
                    e.printStackTrace()
                }.collect{
                    _onDutyStatus.value = it
                }
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        }
    }
}