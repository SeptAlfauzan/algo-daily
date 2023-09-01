package com.septalfauzan.algotrack.domain.model

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import java.util.Date

data class AttendanceRequestBody(
    val status: AttendanceStatus,
    val latitude: Double?,
    val longitude: Double?,
    val reason: String?,
)
