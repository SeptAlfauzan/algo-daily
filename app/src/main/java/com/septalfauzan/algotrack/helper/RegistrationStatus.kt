package com.septalfauzan.algotrack.helper

import com.septalfauzan.algotrack.data.source.remote.apiResponse.RegisterData

sealed class RegistrationStatus{
    data class Success(val registerData: RegisterData?) : RegistrationStatus()
    data class Error(val errorMessage: String) : RegistrationStatus()
}
