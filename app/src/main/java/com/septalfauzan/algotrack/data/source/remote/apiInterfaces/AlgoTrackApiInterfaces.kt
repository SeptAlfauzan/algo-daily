package com.septalfauzan.algotrack.data.source.remote.apiInterfaces

import com.septalfauzan.algotrack.data.model.AuthData
import com.septalfauzan.algotrack.data.model.apiResponse.AuthResponse
import com.septalfauzan.algotrack.data.model.apiResponse.RegisterResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface AlgoTrackApiInterfaces {
    // TODO: implement all api interfaces
    @POST("users/login")
    suspend fun auth(@Body authData: AuthData) : AuthResponse

    @POST("users")
    suspend fun register(@Body userData: RequestBody) : RegisterResponse
}