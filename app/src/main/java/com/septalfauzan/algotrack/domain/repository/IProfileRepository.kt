package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IProfileRepository {
    suspend fun getUserProfile() : Flow<GetProfileResponse>
    suspend fun updateProfilePic(imageFile: File) : Flow<UpdateUserProfilePicData>

    suspend fun getStats() : Flow<UserStatsResponse>
}