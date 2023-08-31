package com.septalfauzan.algotrack.domain.repository

import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IProfileRepository {
    suspend fun getUserProfile() : Flow<GetProfileResponse>
}