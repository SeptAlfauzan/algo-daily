package com.septalfauzan.algotrack.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.ui.AuthFormUIState
import com.septalfauzan.algotrack.domain.model.UserChangePassword
import com.septalfauzan.algotrack.domain.usecase.IAttendanceHistoryUseCase
import com.septalfauzan.algotrack.domain.usecase.IAuthUseCase
import com.septalfauzan.algotrack.helper.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authUseCase: IAuthUseCase,
    private val historyUseCase: IAttendanceHistoryUseCase
) : ViewModel() {

    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoadingSplash: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLogged: StateFlow<Boolean> = _isLogged
    val isLoadingSplash: StateFlow<Boolean> = _isLoadingSplash

    private val _formUiState = MutableStateFlow(AuthFormUIState())
    val formUiState: StateFlow<AuthFormUIState> = _formUiState.asStateFlow()

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            try {
                if (!authUseCase.checkAuthTokenValid()) logout()
                _isLogged.value = authUseCase.checkAuthTokenValid()
                delay(1000)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoadingSplash.value = false
            }
        }
    }

    fun updateEmail(email: String) {
        val error = when {
            email.isEmpty() -> context.getString(R.string.email_cant_empty)
            !email.isEmailValid() -> context.getString(R.string.invalid_email)
            else -> ""
        }
        _formUiState.value = _formUiState.value.copy(
            email = email,
            emailError = error
        )
    }

    fun updatePassword(password: String) {
        val error = if (password.isEmpty()) context.getString(R.string.password_cant_empty) else ""
        _formUiState.value = _formUiState.value.copy(
            password = password,
            passwordError = error
        )
    }

    private fun updateOnLoading(status: Boolean) {
        _formUiState.value = _formUiState.value.copy(
            onLoading = status
        )
    }

    fun login(onSuccess: () -> Unit) {
        updateEmail(_formUiState.value.email)
        updatePassword(_formUiState.value.password)
        viewModelScope.launch(Dispatchers.IO) {
            updateOnLoading(status = true)
            try {
                authUseCase.login(
                    authFormUIState = _formUiState.value,
                    eventChannel = eventChannel,
                    onSuccess = { onSuccess() })
            } catch (e: Exception) {
                e.printStackTrace()
                eventChannel.send(MyEvent.MessageEvent("error login: ${e.message}"))
            } finally {
                updateOnLoading(status = false)
            }
        }
    }

    fun changePassword(newPassword: UserChangePassword, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCase.changePassword(newPassword, eventChannel, onSuccess = { onSuccess() })
            } catch (e: Exception) {
                eventChannel.send(MyEvent.MessageEvent("error: ${e.message}"))
            }
        }
    }

    fun logout(onSuccess: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCase.logout(eventChannel = eventChannel, onSuccess = { onSuccess() })
                historyUseCase.deleteLocalHistoryAttendances()
                _isLogged.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                eventChannel.send(MyEvent.MessageEvent("error: ${e.message}"))
            }
        }
    }
}