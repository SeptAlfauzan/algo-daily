package com.septalfauzan.algotrack.di.module

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.repository.ProfileRepository
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import com.septalfauzan.algotrack.domain.usecase.IProfileUseCase
import com.septalfauzan.algotrack.domain.usecase.ProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(apiInterface: AlgoTrackApiInterfaces, dataStorePreference: DataStorePreference): IProfileRepository = ProfileRepository(apiInterface, dataStorePreference)

    @Provides
    @Singleton
    fun provideProfileUseCase(profileRepository: IProfileRepository) : IProfileUseCase = ProfileUseCase(profileRepository)
}