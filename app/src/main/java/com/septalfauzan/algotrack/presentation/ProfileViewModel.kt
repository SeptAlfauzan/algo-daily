package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase) : ViewModel() {
    private val _profile = MutableStateFlow<UiState<GetProfileResponse>>(UiState.Loading)
    val profile: StateFlow<UiState<GetProfileResponse>> = _profile

    fun getProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            _profile.value = profileUseCase.getProfile().value
        }
    }

    fun reloadProfile(){
        viewModelScope.launch(Dispatchers.IO) { _profile.value = UiState.Loading }
    }
}