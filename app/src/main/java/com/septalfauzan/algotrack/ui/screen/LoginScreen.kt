@file:OptIn(ExperimentalComposeUiApi::class)

package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.navigation.NavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.ui.AuthFormUIState
import com.septalfauzan.algotrack.ui.component.BottomSheetErrorHandler
import com.septalfauzan.algotrack.ui.component.Header
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    formUIStateFlow: StateFlow<AuthFormUIState>,
    eventMessage: Flow<MyEvent>,
    loginAction: () -> Unit,
) {
    val formUiState by formUIStateFlow.collectAsState()
    var errorMessage: String? by remember{ mutableStateOf(null) }

    LaunchedEffect(Unit){
        eventMessage.collect { event ->
            when(event) {
                is MyEvent.MessageEvent -> errorMessage = event.message
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Header(painterResource(id = R.drawable.login_illustration))
            Text(
                text = stringResource(R.string.login_form_title),
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.size(8.dp))
            LoginForm(
                updateEmail = updateEmail,
                updatePassword = updatePassword,
                formUiState = formUiState,
                onRegisterClick = { navController.navigate("register") },
                onLoginCLick = loginAction
            )
        }
        errorMessage?.let{msg ->
            BottomSheetErrorHandler(message = msg, action = {
                errorMessage = null
            }, dismissLabel = "tutup")
        }
    }
}

@Composable
private fun LoginForm(
    onRegisterClick: () -> Unit,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    onLoginCLick: () -> Unit,
    formUiState: AuthFormUIState,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(horizontal = 76.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RoundedTextInput(
            label = stringResource(R.string.email),
            icon = Icons.Default.Email,
            onChange = { updateEmail(it) },
            value = formUiState.email,
            error = formUiState.emailError.isNotEmpty(),
            errorText = formUiState.emailError,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            withOnBlur = true,
            modifier = Modifier
                .fillMaxWidth()

        )
        RoundedTextInput(
            label = stringResource(R.string.password),
            icon = Icons.Default.Lock,
            onChange = { updatePassword(it) },
            value = formUiState.password,
            error = formUiState.passwordError.isNotEmpty(),
            errorText = formUiState.passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            keyboardAction = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            withOnBlur = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(text = stringResource(id = R.string.dont_have_account))
                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { onRegisterClick() })
            }
        }
        RoundedButton(
            text = stringResource(id = R.string.login),
            onClick = { onLoginCLick() },
            onloading = formUiState.onLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}