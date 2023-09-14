package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.domain.model.Auth
import com.septalfauzan.algotrack.domain.model.UserChangePassword
import com.septalfauzan.algotrack.data.source.remote.apiResponse.AuthResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun setAuthToken(token: String) : Unit
    suspend fun getAuthToken() : Flow<String>
    suspend fun login(data: Auth) : Flow<AuthResponse>
    suspend fun changePassword(newPassword: UserChangePassword) : Flow<RegisterResponse>
    suspend fun logout() : Unit
    suspend fun checkTokenIsValid() : Boolean
}