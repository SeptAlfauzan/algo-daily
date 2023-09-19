package com.septalfauzan.algotrack.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.User
import com.septalfauzan.algotrack.data.repository.MainRepository
import com.septalfauzan.algotrack.domain.model.ui.RegisterFormUiState
import com.septalfauzan.algotrack.helper.RegistrationStatus
import com.septalfauzan.algotrack.helper.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MainRepository
) : ViewModel() {

    private val _registrationStatusFlow = MutableStateFlow<RegistrationStatus?>(null)
    val registrationStatusFlow: StateFlow<RegistrationStatus?> = _registrationStatusFlow

    private val _registerFormUiState = MutableStateFlow(RegisterFormUiState())
    val registerFormUiState: StateFlow<RegisterFormUiState> = _registerFormUiState

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun updateEmail(email: String) {
        val error = when {
            email.isEmpty() -> context.getString(R.string.email_cant_empty)
            !email.isEmailValid() -> context.getString(R.string.invalid_email)
            else -> ""
        }
        _registerFormUiState.value = _registerFormUiState.value.copy(
            email = email,
            emailError = error
        )
    }

    fun updatePassword(password: String) {
        val error = if (password.isEmpty()) context.getString(R.string.password_cant_empty) else ""
        _registerFormUiState.value = _registerFormUiState.value.copy(
            password = password,
            passwordError = error
        )
    }

    fun updateName(name: String) {
        val error = if (name.isEmpty()) context.getString(R.string.name_cant_empty) else ""
        _registerFormUiState.value = _registerFormUiState.value.copy(
            name = name,
            nameError = error
        )
    }

    private fun updateOnLoading(status: Boolean) {
        _registerFormUiState.value = _registerFormUiState.value.copy(
            onLoading = status
        )
    }

    fun registerUser(userData: User, onSuccess: () -> Unit) {
        updateEmail(_registerFormUiState.value.email)
        updateName(_registerFormUiState.value.name)
        updatePassword(_registerFormUiState.value.password)

        _registerFormUiState.value.let {
            if (it.emailError.isNotEmpty() || it.nameError.isNotEmpty() || it.passwordError.isNotEmpty()) return
        }

        viewModelScope.launch(Dispatchers.IO) {
            updateOnLoading(true)
            try {
                val registerResponse = repository.registerUserRawJson(userData)
                if (registerResponse.data != null) {
                    withContext(Dispatchers.Main) { onSuccess() }
                } else eventChannel.send(
                    MyEvent.MessageEvent(context.getString(R.string.error_register))
                )
            } catch (e: Exception) {
                eventChannel.send(MyEvent.MessageEvent("error: ${e.message}"))
                _registrationStatusFlow.value =
                    RegistrationStatus.Error(
                        e.message ?: context.getString(R.string.register_failed)
                    )
            } finally {
                updateOnLoading(false)
            }
        }
    }
}
