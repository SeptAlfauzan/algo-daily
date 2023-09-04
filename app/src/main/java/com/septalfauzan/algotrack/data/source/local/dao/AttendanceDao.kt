package com.septalfauzan.algotrack.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(attendanceEntities: List<AttendanceEntity>) : List<Long>

    @Insert
    suspend fun insert(attendanceEntity: AttendanceEntity) : Long

    @Query("DELETE FROM attendance")
    suspend fun deleteAll()


    @Query("SELECT *  FROM attendance WHERE strftime('%d/%m/%Y', datetime(timestamp, 'utc'))  = :date ORDER BY CASE WHEN :sortDirection = 'ASC' THEN :column END ASC, CASE WHEN :sortDirection = 'DESC' THEN :column END DESC")
    suspend fun getByDate(date: String, column: String, sortDirection: String) : List<AttendanceEntity>

    @RawQuery
    suspend fun getHistory(query: SupportSQLiteQuery): List<AttendanceEntity>
}