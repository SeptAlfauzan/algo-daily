package com.septalfauzan.algotrack.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PendingAttendanceDao {
    @Insert
    suspend fun insert(pendingAttendanceEntity: PendingAttendanceEntity) : Long

    @Query("DELETE FROM pending_attendance")
    suspend fun deleteAll()

    @Query("SELECT * FROM pending_attendance")
    suspend fun get():  List<PendingAttendanceEntity>

    @Query("DELETE FROM pending_attendance WHERE id = :id")

    suspend fun delete(id: String) : Int
}