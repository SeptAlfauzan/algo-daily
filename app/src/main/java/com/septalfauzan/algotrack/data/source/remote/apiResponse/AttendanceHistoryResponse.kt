package com.septalfauzan.algotrack.data.source.remote.apiResponse

import com.google.gson.annotations.SerializedName

data class AttendanceHistoryResponse(
    @field:SerializedName("data")
    val data: List<AttendanceResponseData>
)
