package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: IProfileRepository) :
    IProfileUseCase {
    override suspend fun getProfile(): Flow<GetProfileResponse> {
        return profileRepository.getUserProfile()
    }

    override suspend fun updateImageProfile(imageFile: File): Flow<UpdateUserProfilePicData> {
        return profileRepository.updateProfilePic(imageFile)
    }

    override suspend fun getStats(): Flow<UserStatsResponse> {
        return profileRepository.getStats()
    }
}