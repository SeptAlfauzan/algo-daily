package com.septalfauzan.algotrack.domain.model

import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse

data class HomeData(
    val profile: GetProfileResponse,
    val authToken: String,
)
