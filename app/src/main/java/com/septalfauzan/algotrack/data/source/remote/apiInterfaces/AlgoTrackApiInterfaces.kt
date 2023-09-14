package com.septalfauzan.algotrack.data.source.remote.apiInterfaces

import com.septalfauzan.algotrack.data.source.remote.apiResponse.*
import com.septalfauzan.algotrack.domain.model.Attendance
import com.septalfauzan.algotrack.domain.model.Auth
import com.septalfauzan.algotrack.domain.model.UserChangePassword
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AlgoTrackApiInterfaces {
    @POST("users/login")
    suspend fun auth(@Body authData: Auth) : Response<AuthResponse>

    @POST("users")
    suspend fun register(@Body userData: RequestBody) : Response<RegisterResponse>

    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") authToken: String) : Response<GetProfileResponse>

    @GET("users/attendance/history")
    suspend fun getHistory(@Header("Authorization") authToken: String) : Response<AttendanceHistoryResponse>

    @GET("users/attendance/history/{id}")
    suspend fun getDetailHistory(@Header("Authorization") authToken: String, @Path("id") id: String) : Response<AttendanceResponse>

    @POST("users/attendance")
    suspend fun postAttendance(
        @Header("Authorization") authToken: String,
        @Body attendance: Attendance
    ) : Response<AttendanceResponse>

    @PUT("users/attendance/history/{id}")
    suspend fun putAttendance(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,
        @Body attendance: Attendance
    ) : Response<AttendanceResponse>

    @PUT("users/password")
    suspend fun updatePassword(
        @Header("Authorization") authToken: String,
        @Body newPassword : UserChangePassword,
    ) : Response<RegisterResponse>

    @Multipart
    @PUT("users/photo")
    suspend fun updatePP(
        @Header("Authorization") authToken: String,
        @Part photo: MultipartBody.Part
    ) : Response<UpdateUserProfilePicData>

    @GET("users/statistic")
    suspend fun getStats(
        @Header("Authorization") authToken: String
    ) : Response<UserStatsResponse>
}