package com.septalfauzan.algotrack.di.module

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.repository.OnDutyStatusRepository
import com.septalfauzan.algotrack.domain.repository.IOnDutyStatusRepository
import com.septalfauzan.algotrack.domain.usecase.IOnDutyUseCase
import com.septalfauzan.algotrack.domain.usecase.OnDutyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnDutyModule {
    @Provides
    @Singleton
    fun provideOnDutyRepository(
        dataStorePreference: DataStorePreference
    ) : IOnDutyStatusRepository = OnDutyStatusRepository(dataStorePreference)

    @Provides
    @Singleton
    fun provideOnDutyUseCase(
        onDutyStatusRepository: IOnDutyStatusRepository
    ) : IOnDutyUseCase = OnDutyUseCase(onDutyStatusRepository)
}