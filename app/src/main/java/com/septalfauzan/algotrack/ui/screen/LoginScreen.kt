@file:OptIn(ExperimentalComposeUiApi::class)

package com.septalfauzan.algotrack.ui

import android.widget.Toast
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
import androidx.compose.ui.focus.onFocusChanged
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
import com.septalfauzan.algotrack.ui.component.Header
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput
import com.septalfauzan.algotrack.viewmodels.AuthFormUIState
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
    val context = LocalContext.current

    LaunchedEffect(Unit){
        eventMessage.collect { event ->
            when(event) {
                is MyEvent.MessageEvent -> Toast.makeText( context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
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
}

@Composable
private fun LoginForm(
    onRegisterClick: () -> Unit,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    onLoginCLick: () -> Unit,
    formUiState: AuthFormUIState,
) {

    var emailBlur by remember {mutableStateOf(false) }
    var passwordBlur by remember {mutableStateOf(false) }

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
            modifier = Modifier.fillMaxWidth()
        )
    }
}