package com.septalfauzan.algotrack.domain.model.apiResponse

import com.google.gson.annotations.SerializedName

data class AttendanceHistoryResponse(
    @field:SerializedName("attendance_history_response_data")
    val data: List<AttendanceResponseData>
)
