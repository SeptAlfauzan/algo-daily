package com.septalfauzan.algotrack.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceDao
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity

@Database(
    entities = [AttendanceEntity::class],
    version = 1,
    exportSchema = true
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDao

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