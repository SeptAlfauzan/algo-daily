package com.septalfauzan.algotrack.data.source.local.dao

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

enum class AttendanceStatus{
    ON_DUTY,
    PERMIT,
    NOT_FILLED,
    OFF_DUTY
}

@Parcelize
@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.TEXT)
    val id: String,

    @ColumnInfo(name = "status", typeAffinity = ColumnInfo.TEXT)
    val status: AttendanceStatus,

    @ColumnInfo(name = "reason", typeAffinity = ColumnInfo.TEXT)
    val reason: String? = null,

    @ColumnInfo(name = "latitude", typeAffinity = ColumnInfo.REAL)
    val latitude: Double,

    @ColumnInfo(name = "longitude", typeAffinity = ColumnInfo.REAL)
    val longitude: Double,

    @ColumnInfo(name = "timestamp", typeAffinity = ColumnInfo.TEXT)
    val timestamp: String,

    @ColumnInfo(name = "created_at", typeAffinity = ColumnInfo.TEXT)
    val createdAt: String,
) : Parcelable