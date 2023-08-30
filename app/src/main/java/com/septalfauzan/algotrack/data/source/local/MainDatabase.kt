package com.septalfauzan.algotrack.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceDao
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceDao
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity

@Database(
    entities = [AttendanceEntity::class, PendingAttendanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDao
    abstract fun pendingAttendanceDao(): PendingAttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "algo_daily_db"
                    ).build()
                }
                return INSTANCE as MainDatabase
            }
        }
    }
}