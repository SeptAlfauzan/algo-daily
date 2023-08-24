package com.septalfauzan.algotrack.data.model.apiResponse

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(

	@field:SerializedName("attendance_response_data")
	val data: AttendanceResponseData? = null
)

data class AttendanceResponseData(

	@field:SerializedName("attended")
	val attended: Boolean? = null,

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("latitude")
	val latitude: Int? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null,

	@field:SerializedName("longitude")
	val longitude: Int? = null
)
