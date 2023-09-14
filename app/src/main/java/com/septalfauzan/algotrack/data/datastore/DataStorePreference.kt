package com.septalfauzan.algotrack.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.septalfauzan.algotrack.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class SortType{
    ASC,
    DESC
}

enum class SortBy{
    STATUS,
    CREATED_AT
}

class DataStorePreference @Inject constructor(@ApplicationContext private val context: Context){
    private val AUTH_TOKEN = stringPreferencesKey(R.string.auth_token.toString())
    private val DARK_THEME = booleanPreferencesKey(R.string.dark_theme.toString())
    private val ON_DUTY = booleanPreferencesKey(R.string.on_duty.toString())
    private val ATTENDANCE_SORT_BY_STATUS_TYPE = stringPreferencesKey(R.string.attendance_sort_status_type.toString())
    private val ATTENDANCE_SORT_BY_TIME_TYPE = stringPreferencesKey(R.string.attendance_sort_time_type.toString())
    private val ATTENDANCE_SORTBY = stringPreferencesKey(R.string.attendance_sort_by.toString())
    fun getAuthToken(): Flow<String> = context.datastore.data.map { it[AUTH_TOKEN] ?: "" }

    fun getDarkThemeValue(): Flow<Boolean> = context.datastore.data.map { it[DARK_THEME] ?: false }

    fun getOnDutyValue(): Flow<Boolean> = context.datastore.data.map { it[ON_DUTY] ?: true }

    fun getSortByTimeType(): Flow<String> = context.datastore.data.map { it[ATTENDANCE_SORT_BY_TIME_TYPE] ?: SortType.ASC.toString() }
    fun getSortByStatusType(): Flow<String> = context.datastore.data.map { it[ATTENDANCE_SORT_BY_STATUS_TYPE] ?: SortType.ASC.toString() }
    fun getSortBy(): Flow<String> = context.datastore.data.map { it[ATTENDANCE_SORTBY] ?: SortBy.CREATED_AT.toString() }

    suspend fun setAuthToken(token: String) = context.datastore.edit { it[AUTH_TOKEN] = token }
    suspend fun setDarkTheme(value: Boolean) = context.datastore.edit { it[DARK_THEME] = value }
    suspend fun setOnDuty(value: Boolean) = context.datastore.edit { it[ON_DUTY] = value }

    suspend fun setSortByTimestampType(value: SortType) = context.datastore.edit { it[ATTENDANCE_SORT_BY_TIME_TYPE] = value.toString()}
    suspend fun setSortByStatusType(value: SortType) = context.datastore.edit { it[ATTENDANCE_SORT_BY_STATUS_TYPE] = value.toString()}
    suspend fun setSortBy(value: SortBy) = context.datastore.edit { it[ATTENDANCE_SORTBY] = value.toString() }

    companion object {
        @Volatile
        var INSTANCE: DataStorePreference? = null

        fun getInstances(context: Context): DataStorePreference = INSTANCE ?: synchronized(this) {
            val instance = DataStorePreference(context)
            INSTANCE = instance
            instance
        }
    }
}