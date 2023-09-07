package com.septalfauzan.algotrack.domain.model

import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import com.septalfauzan.algotrack.data.ui.UiState

data class HomeData(
    val profile: UiState<GetProfileResponse>,
    val stats: UiState<UserStatsResponse>,
)
