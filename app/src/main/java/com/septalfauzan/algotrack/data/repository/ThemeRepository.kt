package com.septalfauzan.algotrack.data.repository

import android.content.Context
import android.util.Log
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.model.AuthData
import com.septalfauzan.algotrack.data.model.apiResponse.AuthResponse
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStorePreference: DataStorePreference
) {

    suspend fun setDarkThemeValue(value: Boolean){ dataStorePreference.setDarkTheme(value) }

    fun getDarkThemeValueFlow(): Flow<Boolean> = dataStorePreference.getDarkThemeValue()
}