package com.septalfauzan.algotrack.domain.model.ui

data class RegisterFormUiState(
    val email: String = "",
    val emailBlur: Boolean = false,
    val emailError: String = "",
    val name: String = "",
    val nameBlur: Boolean = false,
    val nameError: String = "",
    val password: String = "",
    val passwordBlur: Boolean = false,
    val passwordError: String = "",
    val onLoading: Boolean = false,
)
