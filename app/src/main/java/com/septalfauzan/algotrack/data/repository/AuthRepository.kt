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

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServices: AlgoTrackApiInterfaces,
    private val dataStorePreference: DataStorePreference
) {
    suspend fun login(authData: AuthData): Flow<AuthResponse> {
        try {
            val result = apiServices.auth(authData)
            return flowOf(result)
        }catch (e: Exception){
            throw e
        }
    }

    suspend fun setAuthToken(token: String){
        Log.d("TAG", "setAuthToken: $token")
        dataStorePreference.setAuthToken(token)
    }

    suspend fun getAuthToken(): String = dataStorePreference.getAuthToken().first()

    fun getAuthTokenFlow(): Flow<String> = dataStorePreference.getAuthToken()

    suspend fun logout(){
        dataStorePreference.setAuthToken("")
    }
}