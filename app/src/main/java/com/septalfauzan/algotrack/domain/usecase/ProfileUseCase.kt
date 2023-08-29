package com.septalfauzan.algotrack.domain.usecase

import android.util.Log
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: IProfileRepository) :
    IProfileUseCase {
    override suspend fun getProfile(): StateFlow<UiState<GetProfileResponse>> {
        val result = MutableStateFlow<UiState<GetProfileResponse>>(UiState.Loading)
        try {
            profileRepository.getUserProfile().catch { error ->
                result.value = UiState.Error("Error: ${error.message}")
            }.collect { response ->
                result.value = UiState.Success(response)
            }
        } catch (e: Exception) {
            result.value = UiState.Error("Error: ${e.message}")
        }
        return result
    }

    override suspend fun updateImageProfile(): StateFlow<UiState<String>> {
        // TODO: implement upload user image profile change
        return MutableStateFlow(UiState.Loading)
    }
}