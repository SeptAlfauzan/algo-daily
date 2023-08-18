package com.septalfauzan.algotrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.data.model.AuthData
import com.septalfauzan.algotrack.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AuthFormUIState(
    val email: String = "",
    val emailBlur: Boolean = false,
    val emailError: String = "",
    val password: String = "",
    val passwordBlur: Boolean = false,
    val passwordError: String = "",
)

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoadingSplash: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLogged: StateFlow<Boolean> = _isLogged
    val isLoadingSplash: StateFlow<Boolean> = _isLoadingSplash

    private val _formUiState = MutableStateFlow(AuthFormUIState())
    val formUiState: StateFlow<AuthFormUIState> = _formUiState.asStateFlow()

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val token = authRepository.getAuthToken()
            _isLogged.value = token.isNotEmpty()
            Log.d(AuthViewModel::class.java.simpleName, "auth token: $token")
            delay(1000)
            _isLoadingSplash.value = false
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

        _formUiState.value.let {
            if(it.emailError.isNotEmpty() || it.passwordError.isNotEmpty()) return
        }

        val body = AuthData(email = _formUiState.value.email, password = _formUiState.value.password)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.login(body).catch { error ->
                    error.printStackTrace()
                }.collect { response ->
                    setAuthToken(response.data.token)
                    withContext(Dispatchers.Main) { onSuccess() }
                }
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
                setAuthToken("")
                withContext(Dispatchers.Main) { onSuccess() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setAuthToken(token: String) {
        viewModelScope.launch {
            try {
                authRepository.setAuthToken(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}