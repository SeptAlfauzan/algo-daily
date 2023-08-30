package com.septalfauzan.algotrack.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(attendanceEntities: List<AttendanceEntity>) : List<Long>

    @Insert
    suspend fun insert(attendanceEntity: AttendanceEntity) : Long

    @Query("DELETE FROM attendance")
    suspend fun deleteAll()

    @Query("SELECT *  FROM attendance WHERE  strftime('%d/%m/%Y', datetime(timestamp, 'utc'))  = :date")
    suspend fun getByDate(date: String) : List<AttendanceEntity>
}