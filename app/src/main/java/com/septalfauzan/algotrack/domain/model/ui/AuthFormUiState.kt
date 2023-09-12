package com.septalfauzan.algotrack.domain.model.ui

data class AuthFormUIState(
    val email: String = "",
    val emailBlur: Boolean = false,
    val emailError: String = "",
    val password: String = "",
    val passwordBlur: Boolean = false,
    val passwordError: String = "",
    val onLoading: Boolean = false,
)
