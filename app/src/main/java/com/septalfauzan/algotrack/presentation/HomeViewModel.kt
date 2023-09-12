package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.domain.model.ui.UiState
import com.septalfauzan.algotrack.domain.model.HomeData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.usecase.AuthUseCase
import com.septalfauzan.algotrack.domain.usecase.IAuthUseCase
import com.septalfauzan.algotrack.domain.usecase.IProfileUseCase
import com.septalfauzan.algotrack.domain.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val profileUseCase: IProfileUseCase, private val authUseCase: IAuthUseCase) : ViewModel() {
    private val _homeData = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val homeData: StateFlow<UiState<HomeData>> = _homeData

    fun getHomeData(){

    }
}