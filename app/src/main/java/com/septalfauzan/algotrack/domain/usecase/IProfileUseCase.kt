package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IProfileUseCase {
    suspend fun getProfile() : Flow<GetProfileResponse>
    suspend fun updateImageProfile() : Flow<String>
}