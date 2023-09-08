package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import com.septalfauzan.algotrack.domain.model.HomeData
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.zip
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

    override suspend fun getProfileWithStats(): Flow<HomeData> {
        val profileFlow = profileRepository.getUserProfile()
        val statsFlow = profileRepository.getStats()
        return profileFlow.zip(statsFlow){first, second -> HomeData(profile = first, stats = second)}
    }
}