package com.septalfauzan.algotrack.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(attendanceEntities: List<AttendanceEntity>) : List<Long>

    @Insert
    suspend fun insert(attendanceEntity: AttendanceEntity) : Long

    @Query("DELETE FROM attendance")
    suspend fun deleteAll() : Int
    @RawQuery
    suspend fun getHistory(query: SupportSQLiteQuery): List<AttendanceEntity>

    @Transaction
    open suspend fun  resetThenInsertBatch(attendanceEntities: List<AttendanceEntity>){
        deleteAll()
        insertBatch(attendanceEntities)
    }
}