package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.domain.model.AuthData
import com.septalfauzan.algotrack.domain.model.apiResponse.AuthResponse
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun setAuthToken(token: String) : Unit
    suspend fun getAuthToken() : Flow<String>
    suspend fun login(data: AuthData) : Flow<AuthResponse>
    suspend fun logout() : Unit
}