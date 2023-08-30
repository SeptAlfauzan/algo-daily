package com.septalfauzan.algotrack.di.module

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.repository.AttendanceRepository
import com.septalfauzan.algotrack.data.source.local.MainDatabase
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import com.septalfauzan.algotrack.domain.repository.IAuthRepository
import com.septalfauzan.algotrack.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AttendanceModule {
    @Provides
    @Singleton
    fun provideAttendanceRepository(
        apiServices: AlgoTrackApiInterfaces,
        appDatabase: MainDatabase,
        dataStorePreference: DataStorePreference
    ) : IAttendanceRepository = AttendanceRepository(apiServices, appDatabase, dataStorePreference)

    @Provides
    @Singleton
    fun provideHistoryAttendanceUseCase(attendanceRepository: IAttendanceRepository) : IHistoryAttendanceUseCase = HistoryAttendanceUseCase(attendanceRepository)


    @Provides
    @Singleton
    fun providePendingAttendanceUseCase(attendanceRepository: IAttendanceRepository) : IPendingAttendanceUseCase = PendingAttendanceUseCase(attendanceRepository)
}