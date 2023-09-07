package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IProfileUseCase {
    suspend fun getProfile() : Flow<GetProfileResponse>
    suspend fun updateImageProfile(imageFile: File) : Flow<UpdateUserProfilePicData>
    suspend fun getStats() : Flow<UserStatsResponse>
}