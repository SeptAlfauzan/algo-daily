package com.septalfauzan.algotrack.domain.model.apiResponse

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AttendanceResponse(

	@field:SerializedName("attendance_response_data")
	val data: AttendanceResponseData? = null
)

@Parcelize
@Entity(tableName = "attendance")
data class AttendanceResponseData(

	@field:SerializedName("id")
	@PrimaryKey
	@NonNull
	@ColumnInfo(name="id", typeAffinity = ColumnInfo.TEXT)
	val id: String,

	@field:SerializedName("attended")
	@ColumnInfo(name="attended", typeAffinity = ColumnInfo.INTEGER)
	val attended: Boolean? = null,

	@field:SerializedName("reason")
	@ColumnInfo(name="reason", typeAffinity = ColumnInfo.TEXT)
	val reason: String? = null,


	@field:SerializedName("timestamp")
	@ColumnInfo(name="timestamp", typeAffinity = ColumnInfo.TEXT)
	val timestamp: String? = null,

	@field:SerializedName("latitude")
	@ColumnInfo(name="latitude", typeAffinity = ColumnInfo.REAL)
	val latitude: Double? = null,

	@field:SerializedName("longitude")
	@ColumnInfo(name="longitude", typeAffinity = ColumnInfo.REAL)
	val longitude: Double? = null
) : Parcelable
