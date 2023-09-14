package com.septalfauzan.algotrack.domain.model.ui

data class AttendanceFormUiState(
    val selectedAnswer: String = "",
    val reasonNotWork: String = "",
    val reasonNotWorkBlur: Boolean = false,
    val reasonNotWorkdError: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val onLoading: Boolean = false,
)
