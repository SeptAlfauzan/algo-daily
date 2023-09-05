package com.septalfauzan.algotrack.domain.usecase

import android.util.Log
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: IProfileRepository) :
    IProfileUseCase {
    override suspend fun getProfile(): Flow<GetProfileResponse> {
        return profileRepository.getUserProfile()
    }

    override suspend fun updateImageProfile(): Flow<String> {
        // TODO: implement upload user image profile change
        return flowOf("")
    }
}