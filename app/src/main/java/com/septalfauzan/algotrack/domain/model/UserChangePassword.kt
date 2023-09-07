package com.septalfauzan.algotrack.domain.model

data class UserChangePassword(
    val newPassword : String,
    val confirmPassword : String,
)
