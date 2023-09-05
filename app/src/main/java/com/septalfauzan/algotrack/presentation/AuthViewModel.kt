package com.septalfauzan.algotrack.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.AuthData
import com.septalfauzan.algotrack.data.repository.AuthRepository
import com.septalfauzan.algotrack.data.ui.AuthFormUIState
import com.septalfauzan.algotrack.domain.usecase.IAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCase: IAuthUseCase) : ViewModel() {

    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoadingSplash: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLogged: StateFlow<Boolean> = _isLogged
    val isLoadingSplash: StateFlow<Boolean> = _isLoadingSplash

    private val _formUiState = MutableStateFlow(AuthFormUIState())
    val formUiState: StateFlow<AuthFormUIState> = _formUiState.asStateFlow()

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        // TODO: fix splash screen issue, login screen flash even tho already login  
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.getAuthToken().catch {
                it.printStackTrace()
                _isLogged.value = false
                delay(1000)
                _isLoadingSplash.value = false
            }.collect{token ->
                _isLogged.value = token.isNotEmpty()
                Log.d(AuthViewModel::class.java.simpleName, "auth token: $token")
                delay(1000)
                _isLoadingSplash.value = false
            }
        }
    }
    fun updateEmail(email: String){
        val error = if(email.isEmpty()) "Field email tidak boleh kosong!" else ""
        _formUiState.value = _formUiState.value.copy(
            email = email,
            emailError = error
        )
    }
    fun updatePassword(password: String){
        val error = if(password.isEmpty()) "Field password tidak boleh kosong!" else ""
        _formUiState.value = _formUiState.value.copy(
            password = password,
            passwordError = error
        )
    }
    /**
     * @see this function doesnt require body of username and password sent through parameter because it state already in viewmodel
     * @param onSuccess define navigation or anything that should be execute when login is success, this onSuccess is run in main thread
     */
    fun login(onSuccess: () -> Unit) {
        updateEmail(_formUiState.value.email)
        updatePassword(_formUiState.value.password)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCase.login(authFormUIState = _formUiState.value, eventChannel = eventChannel, onSuccess = { onSuccess() })
            } catch (e: Exception) {
                Log.d("TAG", "login error: ${e.message}")
                e.printStackTrace()
                eventChannel.send(MyEvent.MessageEvent("error login: ${e.message}"))
            }
        }
    }

    /**
     *@param onSuccess define navigation or anything that should be execute when logout is success, this onSuccess is run in main thread
     */
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCase.logout(eventChannel = eventChannel, onSuccess = { onSuccess() })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}