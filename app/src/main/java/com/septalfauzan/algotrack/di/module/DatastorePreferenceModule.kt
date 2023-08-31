package com.septalfauzan.algotrack.di.module

import android.content.Context
import androidx.room.Room
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.local.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatastorePreferenceModule {

    @Provides
    @Singleton
    fun provideDatastorePreference(@ApplicationContext appContext: Context): DataStorePreference{
        return DataStorePreference.getInstances(appContext)
    }
}