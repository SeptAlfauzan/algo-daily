package com.septalfauzan.algotrack.data.repository

import android.util.Log
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.domain.model.AuthData
import com.septalfauzan.algotrack.domain.model.apiResponse.AuthResponse
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.model.UserChangePassword
import com.septalfauzan.algotrack.domain.model.apiResponse.RegisterResponse
import com.septalfauzan.algotrack.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiServices: AlgoTrackApiInterfaces,
    private val dataStorePreference: DataStorePreference
) :IAuthRepository {
    override suspend fun login(authData: AuthData): Flow<AuthResponse> {
        try {
            val result = apiServices.auth(authData)
            return flowOf(result)
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun changePassword(newPassword: UserChangePassword): Flow<RegisterResponse> {
        val token = dataStorePreference.getAuthToken().first()
        try {
            val result = apiServices.updatePassword(token, newPassword)
            return flowOf(result)
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun setAuthToken(token: String){
        Log.d("TAG", "setAuthToken: $token")
        dataStorePreference.setAuthToken(token)
    }

    override suspend fun getAuthToken(): Flow<String> = dataStorePreference.getAuthToken()

    override suspend fun logout(){
        dataStorePreference.setAuthToken("")
    }
}