package com.septalfauzan.algotrack.domain.model

import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.UserStatsResponse
import com.septalfauzan.algotrack.domain.model.ui.UiState

data class HomeData(
    val profile: GetProfileResponse,
    val stats: UserStatsResponse,
)
