package com.septalfauzan.algotrack.data.source.remote.apiResponse

import com.google.gson.annotations.SerializedName
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.helper.formatToLocaleGMT

data class AttendanceResponse(

	@field:SerializedName("data")
	val data: AttendanceResponseData
)

data class AttendanceResponseData(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double?,

	@field:SerializedName("longitude")
	val longitude: Double?,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("created_at")
	val createdAt: String,
)

fun AttendanceResponseData.toAttendanceEntity(): AttendanceEntity{
	return AttendanceEntity(
		id = this.id,
		status = AttendanceStatus.valueOf(this.status),
		reason = this.reason,
		latitude = this.latitude,
		longitude = this.longitude,
		timestamp = this.timestamp,
		createdAt = this.createdAt
	)
}

fun AttendanceResponseData.formatTimeToGMT(): AttendanceResponseData {
	return AttendanceResponseData(
		id = this.id,
		status = this.status,
		reason = this.reason,
		latitude = this.latitude,
		longitude = this.longitude,
		timestamp = this.timestamp.formatToLocaleGMT(),
		createdAt = this.createdAt.formatToLocaleGMT()
	)
}

