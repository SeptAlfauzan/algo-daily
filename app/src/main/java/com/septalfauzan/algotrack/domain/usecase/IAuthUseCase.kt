package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.data.ui.AuthFormUIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface IAuthUseCase {
    suspend fun login(authFormUIState: AuthFormUIState,eventChannel: Channel<MyEvent>, onSuccess: () -> Unit)
    suspend fun logout(eventChannel: Channel<MyEvent>, onSuccess: () -> Unit)
    suspend fun getAuthToken() : Flow<String>
}