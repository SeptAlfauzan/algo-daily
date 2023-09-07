package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.UpdateUserProfilePicData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IProfileRepository {
    suspend fun getUserProfile() : Flow<GetProfileResponse>
    suspend fun updatePP(imageFile: File) : Flow<UpdateUserProfilePicData>
}