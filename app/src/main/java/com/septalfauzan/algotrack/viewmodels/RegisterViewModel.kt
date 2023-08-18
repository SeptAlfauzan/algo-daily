package com.septalfauzan.algotrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.data.model.UserData
import com.septalfauzan.algotrack.data.repository.MainRepository
import com.septalfauzan.algotrack.helper.RegistrationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _registrationStatusFlow = MutableStateFlow<RegistrationStatus?>(null)
    val registrationStatusFlow: StateFlow<RegistrationStatus?> = _registrationStatusFlow

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun registerUser(userData: UserData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val registerResponse = repository.registerUserRawJson(userData)
//                _registrationStatusFlow.value = RegistrationStatus.Success(registerResponse.data)
                if (registerResponse.data != null) {
                    withContext(Dispatchers.Main) { onSuccess() }
                } else eventChannel.send(
                    MyEvent.MessageEvent("Error when register")
                )
            } catch (e: Exception) {
                eventChannel.send(MyEvent.MessageEvent("error: ${e.message}"))
                _registrationStatusFlow.value =
                    RegistrationStatus.Error(e.message ?: "Registration failed")
            }
        }
    }
}