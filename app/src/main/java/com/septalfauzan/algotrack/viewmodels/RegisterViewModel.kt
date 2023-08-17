package com.septalfauzan.algotrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.model.UserData
import com.septalfauzan.algotrack.data.repository.MainRepository
import com.septalfauzan.algotrack.helper.RegistrationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _registrationStatusFlow = MutableStateFlow<RegistrationStatus?>(null)
    val registrationStatusFlow: StateFlow<RegistrationStatus?> = _registrationStatusFlow

    fun registerUser(userData: UserData) {
        viewModelScope.launch {
            try {
                val registerResponse = repository.registerUserRawJson(userData)
                _registrationStatusFlow.value = RegistrationStatus.Success(registerResponse.data)
            } catch (e: Exception) {
                _registrationStatusFlow.value = RegistrationStatus.Error(e.message ?: "Registration failed")
            }
        }
    }
}