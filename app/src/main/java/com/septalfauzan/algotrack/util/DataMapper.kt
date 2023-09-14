package com.septalfauzan.algotrack.util

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.model.Attendance

object DataMapper {

    fun PendingAttendanceEntity.toAttendance(): Attendance {
        return Attendance(
            status = this.status,
            latitude = this.latitude,
            longitude = this.longitude,
            reason = this.reason,
            created_at = this.createdAt
        )
    }

    fun PendingAttendanceEntity.toAttendanceEntity(): AttendanceEntity {
        return AttendanceEntity(
            id = this.id,
            status = this.status,
            latitude = this.latitude,
            longitude = this.longitude,
            reason = this.reason,
            createdAt = this.createdAt,
            timestamp = this.timestamp,
        )
    }

        fun AttendanceEntity.toAttendance(): Attendance {
        return Attendance(
            status = this.status,
            latitude = this.latitude,
            longitude = this.longitude,
            reason = this.reason,
            created_at = this.createdAt
        )
    }
}