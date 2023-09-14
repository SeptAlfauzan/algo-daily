@file:OptIn(ExperimentalComposeUiApi::class)

package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.ui.RegisterFormUiState
import com.septalfauzan.algotrack.domain.model.User
import com.septalfauzan.algotrack.ui.component.BottomSheetErrorHandler
import com.septalfauzan.algotrack.ui.component.Header
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    RegisterAction: (User) -> Unit,
    LoginAction: () -> Unit,
    registerFormUiStateFlow: StateFlow<RegisterFormUiState>,
    updateName: (String) -> Unit,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    eventMessage: Flow<MyEvent>,
) {
    var errorMessage: String? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        eventMessage.collect { event ->
            when (event) {
                is MyEvent.MessageEvent -> errorMessage = event.message
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Header(painterResource(id = R.drawable.login_illustration))
            Text(
                text = stringResource(R.string.register_form_title),
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.size(8.dp))
            RegisterForm(
                onRegisterClick = RegisterAction,
                registerFormUiState = registerFormUiStateFlow.collectAsState().value,
                updateName = updateName,
                updateEmail = updateEmail,
                updatePassword = updatePassword,
                onLoginCLick = LoginAction
            )
        }
        errorMessage?.let { msg ->
            BottomSheetErrorHandler(message = msg, action = {
                errorMessage = null
            })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RegisterForm(
    onRegisterClick: (User) -> Unit,
    onLoginCLick: () -> Unit,
    registerFormUiState: RegisterFormUiState,
    updateName: (String) -> Unit,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(horizontal = 76.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RoundedTextInput(
            label = stringResource(id = R.string.name),
            icon = Icons.Default.Person,
            onChange = updateName,
            value = registerFormUiState.name,
            error = registerFormUiState.nameError.isNotEmpty(),
            errorText = registerFormUiState.nameError,
            imeAction = ImeAction.Next,
            withOnBlur = true,
            modifier = Modifier.fillMaxWidth()
        )
        RoundedTextInput(
            label = stringResource(id = R.string.email),
            icon = Icons.Default.Email,
            onChange = updateEmail,
            value = registerFormUiState.email,
            error = registerFormUiState.emailError.isNotEmpty(),
            errorText = registerFormUiState.emailError,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            withOnBlur = true,
            modifier = Modifier.fillMaxWidth()
        )
        RoundedTextInput(
            label = stringResource(id = R.string.password),
            icon = Icons.Default.Lock,
            onChange = updatePassword,
            value = registerFormUiState.password,
            error = registerFormUiState.passwordError.isNotEmpty(),
            errorText = registerFormUiState.passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            withOnBlur = true,
            keyboardAction = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(text = stringResource(id = R.string.already_have_account))
                Text(
                    text = stringResource(id = R.string.login),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { onLoginCLick() }
                )
            }
        }
        RoundedButton(
            text = stringResource(R.string.register),
            onloading = registerFormUiState.onLoading,
            onClick = {
                val userData = User(
                    registerFormUiState.name,
                    registerFormUiState.email,
                    registerFormUiState.password
                )
                onRegisterClick(userData)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}