package com.septalfauzan.algotrack.domain.usecase

import android.util.Log
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.ui.AuthFormUIState
import com.septalfauzan.algotrack.domain.model.Auth
import com.septalfauzan.algotrack.domain.model.UserChangePassword
import com.septalfauzan.algotrack.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: IAuthRepository) : IAuthUseCase {
    override suspend fun login(authFormUIState: AuthFormUIState, eventChannel: Channel<MyEvent>, onSuccess: () -> Unit) {

        authFormUIState.let {
            if(it.emailError.isNotEmpty() || it.passwordError.isNotEmpty()) return
        }

        val body = Auth(email = authFormUIState.email, password = authFormUIState.password)
        authRepository.login(body).catch {error ->
            eventChannel.send(MyEvent.MessageEvent("error: ${error.message}"))
        }.collect{
            authRepository.setAuthToken(it.data.token)
            withContext(Dispatchers.Main){ onSuccess() }
        }
    }

    override suspend fun changePassword(
        newPassword: UserChangePassword,
        eventChannel: Channel<MyEvent>,
        onSuccess: () -> Unit
    ) {
        authRepository.changePassword(newPassword).catch { error ->
            eventChannel.send(MyEvent.MessageEvent("error: ${error.message}"))
        }.collect{
            withContext(Dispatchers.Main){
                onSuccess()
            }
        }
    }

    override suspend fun logout(eventChannel: Channel<MyEvent>, onSuccess: () -> Unit) {
        authRepository.setAuthToken("")
        withContext(Dispatchers.Main){ onSuccess() }
    }

    override suspend fun getAuthToken(): Flow<String> = authRepository.getAuthToken()
    override suspend fun checkAuthTokenValid():Boolean = authRepository.checkTokenIsValid()

}