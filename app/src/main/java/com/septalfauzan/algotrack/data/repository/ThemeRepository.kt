package com.septalfauzan.algotrack.data.repository

import android.content.Context
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.domain.repository.IThemeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStorePreference: DataStorePreference
) : IThemeRepository {
    override suspend fun setDarkThemeValue(value: Boolean) {
        dataStorePreference.setDarkTheme(value)
    }
    override fun getDarkThemeValueFlow(): Flow<Boolean> = dataStorePreference.getDarkThemeValue()
}