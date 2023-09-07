package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.UpdateUserProfilePicData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IProfileUseCase {
    suspend fun getProfile() : Flow<GetProfileResponse>
    suspend fun updateImageProfile(imageFile: File) : Flow<UpdateUserProfilePicData>
}