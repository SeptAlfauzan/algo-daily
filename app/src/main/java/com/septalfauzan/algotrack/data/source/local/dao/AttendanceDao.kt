package com.septalfauzan.algotrack.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface AttendanceDao {

    @Insert
    suspend fun insertAll()
}