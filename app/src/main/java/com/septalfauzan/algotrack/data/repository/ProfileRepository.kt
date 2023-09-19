package com.septalfauzan.algotrack.data.repository

import android.util.Log
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import com.septalfauzan.algotrack.helper.RequestError.getErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val apiService: AlgoTrackApiInterfaces,
    private val dataStorePreference: DataStorePreference
) : IProfileRepository {

    override suspend fun getUserProfile(): Flow<GetProfileResponse> {
        val token = dataStorePreference.getAuthToken().first()
        try {
            val result = apiService.getUserProfile(token)
            if(!result.isSuccessful) {
                val errorJson = result.errorBody()?.string()
                val errorResponse = errorJson?.getErrorMessage()
                throw Exception(errorResponse?.errors ?: result.message())
            }
            return flowOf(result.body()!!)
        } catch (e: Exception){
            throw e
        }
    }

    override suspend fun updateProfilePic(imageFile: File): Flow<UpdateUserProfilePicData> {
        val token = dataStorePreference.getAuthToken().first()
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
        try {
            val result = apiService.updatePP(token, photoPart)
            if(!result.isSuccessful) {
                val errorJson = result.errorBody()?.string()
                val errorResponse = errorJson?.getErrorMessage()
                throw Exception(errorResponse?.errors ?: result.message())
            }
            return flowOf(result.body()!!)
        } catch (e: Exception){
            throw e
        }
    }

    override suspend fun getStats(): Flow<UserStatsResponse> {
        val token = dataStorePreference.getAuthToken().first()
        try {
            val result = apiService.getStats(token)
            if(!result.isSuccessful) {
                val errorJson = result.errorBody()?.string()
                val errorResponse = errorJson?.getErrorMessage()
                throw Exception(errorResponse?.errors ?: result.message())
            }
            return flowOf(result.body()!!)
        }catch (e: Exception){
            throw e
        }
    }
}