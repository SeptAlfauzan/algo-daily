package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import kotlinx.coroutines.flow.StateFlow

interface IProfileUseCase {
    suspend fun getProfile() : StateFlow<UiState<GetProfileResponse>>
    suspend fun updateImageProfile() : StateFlow<UiState<String>>
}