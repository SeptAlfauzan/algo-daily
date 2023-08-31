package com.septalfauzan.algotrack.di.module

import android.content.Context
import androidx.room.Room
import com.septalfauzan.algotrack.data.source.local.MainDatabase
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideAttendanceDao(appDatabase: MainDatabase): AttendanceDao {
        return appDatabase.attendanceDao()
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context): MainDatabase{
        return Room.databaseBuilder(
            appContext,
            MainDatabase::class.java,
            "algo_daily_db"
        ).build()
    }
}