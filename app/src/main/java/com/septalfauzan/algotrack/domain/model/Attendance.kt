package com.septalfauzan.algotrack.domain.model

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus

data class Attendance(
    val status: AttendanceStatus,
    val latitude: Double?,
    val longitude: Double?,
    val reason: String?,
    val created_at: String?
)
