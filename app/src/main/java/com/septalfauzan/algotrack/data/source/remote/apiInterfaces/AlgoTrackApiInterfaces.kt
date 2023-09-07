package com.septalfauzan.algotrack.data.source.remote.apiInterfaces

import com.septalfauzan.algotrack.domain.model.AttendanceRequestBody
import com.septalfauzan.algotrack.domain.model.AuthData
import com.septalfauzan.algotrack.domain.model.UserChangePassword
import com.septalfauzan.algotrack.domain.model.apiResponse.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AlgoTrackApiInterfaces {
    // TODO: implement all api interfaces
    @POST("users/login")
    suspend fun auth(@Body authData: AuthData) : AuthResponse

    @POST("users")
    suspend fun register(@Body userData: RequestBody) : RegisterResponse

    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") authToken: String) : GetProfileResponse

    @GET("users/attendance/history")
    suspend fun getHistory(@Header("Authorization") authToken: String) : AttendanceHistoryResponse

    @GET("users/attendance/history/{id}")
    suspend fun getDetailHistory(@Header("Authorization") authToken: String, @Path("id") id: String) : AttendanceResponse

    @POST("users/attendance")
    suspend fun postAttendance(
        @Header("Authorization") authToken: String,
        @Body attendance: AttendanceRequestBody
    ) : AttendanceResponse

    @PUT("users/attendance/history/{id}")
    suspend fun putAttendance(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,
        @Body attendance: AttendanceRequestBody
    ) : AttendanceResponse

    @PUT("users/password")
    suspend fun updatePassword(
        @Header("Authorization") authToken: String,
        @Body newPassword : UserChangePassword,
    ) : RegisterResponse

    @PUT("users/photo")
    suspend fun updatePP(
        @Header("Authorization") authToken: String,
        @Part photo: MultipartBody.Part
    ) : UpdateUserProfilePicData
}