package com.septalfauzan.algotrack.data.repository

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.UpdateUserProfilePicData
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

    override suspend fun updatePP(imageFile: File): Flow<UpdateUserProfilePicData> {
        val token = dataStorePreference.getAuthToken().first()
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
        try {
            return flowOf(apiService.updatePP(token, photoPart))
        } catch (e: Exception){
            throw e
        }
    }
}