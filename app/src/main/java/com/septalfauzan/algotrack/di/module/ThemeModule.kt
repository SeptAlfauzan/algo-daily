package com.septalfauzan.algotrack.di.module

import android.content.Context
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.repository.ThemeRepository
import com.septalfauzan.algotrack.domain.repository.IThemeRepository
import com.septalfauzan.algotrack.domain.usecase.IThemeUseCase
import com.septalfauzan.algotrack.domain.usecase.ThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {
    @Provides
    @Singleton
    fun provideThemeRepository(
        @ApplicationContext context: Context,
        dataStorePreference: DataStorePreference
    ) : IThemeRepository = ThemeRepository(context, dataStorePreference)

    @Provides
    @Singleton
    fun provideThemeUseCase(themeRepository: ThemeRepository) : IThemeUseCase = ThemeUseCase(themeRepository)
}