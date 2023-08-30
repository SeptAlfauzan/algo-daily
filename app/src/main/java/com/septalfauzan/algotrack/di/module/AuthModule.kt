package com.septalfauzan.algotrack.di.module

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.repository.AuthRepository
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.repository.IAuthRepository
import com.septalfauzan.algotrack.domain.usecase.AuthUseCase
import com.septalfauzan.algotrack.domain.usecase.IAuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        apiServices: AlgoTrackApiInterfaces,
        dataStorePreference: DataStorePreference
    ) : IAuthRepository = AuthRepository(apiServices, dataStorePreference)

    @Provides
    @Singleton
    fun provideAuthUseCase(authRepository: IAuthRepository) : IAuthUseCase = AuthUseCase(authRepository)
}