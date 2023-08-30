package com.septalfauzan.algotrack.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PendingAttendanceDao {
    @Insert
    suspend fun insert(pendingAttendanceEntity: PendingAttendanceEntity) : Long

    @Query("DELETE FROM pending_attendance")
    suspend fun deleteAll()
}