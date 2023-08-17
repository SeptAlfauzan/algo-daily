package com.septalfauzan.algotrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.model.AuthData
import com.septalfauzan.algotrack.data.repository.AuthRepository
import com.septalfauzan.algotrack.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoadingSplash: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLogged: StateFlow<Boolean> = _isLogged
    val isLoadingSplash: StateFlow<Boolean> = _isLoadingSplash


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val token = authRepository.getAuthToken()
            _isLogged.value = token.isNotEmpty()
            Log.d(AuthViewModel::class.java.simpleName, "auth token: $token")
            delay(1000)
            _isLoadingSplash.value = false
        }
    }
    /**
     * @param body data such as email and password for login purpose
     * @param onSuccess define navigation or anything that should be execute when login is success, this onSuccess is run in main thread
     */
    fun login(body: AuthData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.login(body).catch { error ->
                    error.printStackTrace()
                }.collect { data ->
                    setAuthToken(data)
                    withContext(Dispatchers.Main) { onSuccess() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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