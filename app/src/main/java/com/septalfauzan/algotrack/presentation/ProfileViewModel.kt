package com.septalfauzan.algotrack.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UpdateUserProfilePicData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import com.septalfauzan.algotrack.domain.model.HomeData
import com.septalfauzan.algotrack.domain.usecase.IProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: IProfileUseCase) :
    ViewModel() {
    private val _profile = MutableStateFlow<UiState<GetProfileResponse>>(UiState.Loading)
    val profile: StateFlow<UiState<GetProfileResponse>> = _profile

    private val _stats = MutableStateFlow<UiState<UserStatsResponse>>(UiState.Loading)
    val stats: StateFlow<UiState<UserStatsResponse>> = _stats

    private val _changePP = MutableStateFlow<UiState<UpdateUserProfilePicData>>(UiState.Loading)
    val changePP: StateFlow<UiState<UpdateUserProfilePicData>> = _changePP

    private val _homeData = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val homeData: StateFlow<UiState<HomeData>> = _homeData

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun getProfile() {
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

    fun reloadProfile() {
        viewModelScope.launch(Dispatchers.IO) { _profile.value = UiState.Loading }
    }

    fun updatePP(imageFile: File, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileUseCase.updateImageProfile(imageFile).catch { error ->
                    _changePP.value = UiState.Error("Error: ${error.message}")
                    eventChannel.send(MyEvent.MessageEvent("Error: ${error.message}"))
                }.collect { response ->
                    _changePP.value = UiState.Success(response)
                    withContext(Dispatchers.Main) { onSuccess() }
                }
            } catch (e: Exception) {
                _changePP.value = UiState.Error("Error: ${e.message}")
                eventChannel.send(MyEvent.MessageEvent("Error: ${e.message}"))
            }
        }
    }

    private fun getStats() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileUseCase.getStats().catch { e ->
                    _stats.value = UiState.Error("Error: ${e.message}")
                }.collect {
                    _stats.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _stats.value = UiState.Error("Error: ${e.message}")
            }
        }
    }

    fun getProfileWithStats() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileUseCase.getProfileWithStats().catch { e ->
                    _homeData.value = UiState.Error("Error: ${e.message}")
                }.collect {
                    _homeData.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _homeData.value = UiState.Error("Error: ${e.message}")
            }
        }
    }

    fun reloadProfileWithStats(){
        _homeData.value = UiState.Loading
    }
}