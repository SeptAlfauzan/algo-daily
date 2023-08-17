package com.septalfauzan.algotrack.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStorePreference @Inject constructor(@ApplicationContext private val context: Context){
    private val AUTH_TOKEN = stringPreferencesKey("auth_token")

    fun getAuthToken(): Flow<String> = context.datastore.data.map { it[AUTH_TOKEN] ?: "" }

    suspend fun setAuthToken(token: String){
        context.datastore.edit { it[AUTH_TOKEN] = token }
    }

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