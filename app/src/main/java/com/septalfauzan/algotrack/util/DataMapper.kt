package com.septalfauzan.algotrack.util

import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.domain.model.Attendance

object DataMapper {

    fun PendingAttendanceEntity.toAttendance(): Attendance {
        return Attendance(
            status = this.status,
            latitude = this.latitude,
            longitude = this.longitude,
            reason = this.reason,
        )
    }

    fun AttendanceEntity.toAttendance(): Attendance {
        return Attendance(
            status = this.status,
            latitude = this.latitude,
            longitude = this.longitude,
            reason = this.reason,
        )
    }
}