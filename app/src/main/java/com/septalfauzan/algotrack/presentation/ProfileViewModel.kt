package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.domain.usecase.IProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: IProfileUseCase) : ViewModel() {
    private val _profile = MutableStateFlow<UiState<GetProfileResponse>>(UiState.Loading)
    val profile: StateFlow<UiState<GetProfileResponse>> = _profile

    private val _changePP = MutableStateFlow<UiState<UpdateUserProfilePicData>>(UiState.Loading)
    val changePP: StateFlow<UiState<UpdateUserProfilePicData>> = _changePP

    fun getProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileUseCase.getProfile().catch { error ->
                    _profile.value = UiState.Error("Error: ${error.message}")
                }.collect { response ->
                    _profile.value = UiState.Success(response)
                }
            } catch (e: Exception) {
                _profile.value = UiState.Error("Error: ${e.message}")
            }
        }
    }

    fun reloadProfile(){
        viewModelScope.launch(Dispatchers.IO) { _profile.value = UiState.Loading }
    }

    fun updatePP(imageFile: File){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileUseCase.updateImageProfile(imageFile).catch { error ->
                    _changePP.value = UiState.Error("Error: ${error.message}")
                }.collect { response ->
                    _changePP.value = UiState.Success(response)
                }
            } catch (e: Exception) {
                _changePP.value = UiState.Error("Error: ${e.message}")
            }
        }
    }
}