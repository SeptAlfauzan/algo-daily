package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.septalfauzan.algotrack.data.repository.AttendanceRepository
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.domain.model.UserLocation
import com.septalfauzan.algotrack.helper.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository) : ViewModel() {
    private var userLocation: UserLocation? = null
    fun postAttendance(
        selectedAnswer: String,
        reasonNotWork: String,
        navController: NavController
    ) {
        val status = if (selectedAnswer == "Yes") AttendanceStatus.ON_DUTY else AttendanceStatus.OFF_DUTY
        val attendanceEntity = AttendanceEntity(
            id = UUID.randomUUID().toString(),
            status = status,
            reason = if (selectedAnswer == "No") reasonNotWork else null,
            latitude = userLocation?.latitude,
            longitude = userLocation?.longitude,
            timestamp = "current_timestamp",
            createdAt = "current_timestamp"
        )

        viewModelScope.launch {
            repository.updateAttendance(attendanceEntity)
            navController.navigate(Screen.Success.route)
        }
    }
}