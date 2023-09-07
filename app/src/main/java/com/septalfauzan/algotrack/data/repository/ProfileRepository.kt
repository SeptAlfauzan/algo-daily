package com.septalfauzan.algotrack.data.repository

import android.util.Log
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
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
            return flowOf(apiService.getUserProfile(token))
        } catch (e: Exception){
            throw e
        }
    }

    override suspend fun updateProfilePic(imageFile: File): Flow<UpdateUserProfilePicData> {
        val token = dataStorePreference.getAuthToken().first()
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
    Log.d("TAG", "updateProfilePic:  $requestFile")
        val photoPart = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
        try {
            return flowOf(apiService.updatePP(token, photoPart))
        } catch (e: Exception){
            Log.d("TAG", "updateProfilePic: error: $e")
            throw e
        }
    }

    override suspend fun getStats(): Flow<UserStatsResponse> {
        val token = dataStorePreference.getAuthToken().first()
        try {
            return flowOf(apiService.getStats(token))
        }catch (e: Exception){
            throw e
        }
    }
}