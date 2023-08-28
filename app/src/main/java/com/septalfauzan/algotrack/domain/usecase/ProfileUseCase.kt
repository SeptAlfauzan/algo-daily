package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: IProfileRepository) : IProfileUseCase {
    override suspend fun getProfile(): StateFlow<UiState<GetProfileResponse>> = profileRepository.getUserProfile()

    override suspend fun updateImageProfile(): StateFlow<UiState<String>> {
        // TODO: implement upload user image profile change
        return MutableStateFlow(UiState.Loading)
    }
}