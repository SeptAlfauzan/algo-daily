@file:OptIn(ExperimentalComposeUiApi::class)

package com.septalfauzan.algotrack.ui

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
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
import com.septalfauzan.algotrack.data.model.UserData
import com.septalfauzan.algotrack.ui.component.Header
import com.septalfauzan.algotrack.ui.component.LogRegButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput
import kotlinx.coroutines.flow.Flow

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    RegisterAction: (UserData) -> Unit,
    LoginAction: () -> Unit,
    eventMessage: Flow<MyEvent>,
){

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
            text = stringResource(R.string.register_form_title),
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.size(8.dp))
        RegisterForm(
            onRegisterClick = RegisterAction,
            onLoginCLick = LoginAction
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RegisterForm(onRegisterClick: (UserData) -> Unit, onLoginCLick: () -> Unit){

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(horizontal = 76.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RoundedTextInput(
            label = "Nama",
            icon = Icons.Default.Person,
            onChange = { name = it },
            value = name,
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )
        RoundedTextInput(
            label = "Email",
            icon = Icons.Default.Email,
            onChange = { email = it },
            value = email,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )
        RoundedTextInput(
            label = "Password",
            icon = Icons.Default.Lock,
            onChange = { password = it },
            value = password,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
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
        if (showError) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){Text(
                text = "All fields must be filled",
                color = Color.Red,
                style = MaterialTheme.typography.caption
            )
            }

        }
        LogRegButton(
            text = stringResource(R.string.register),
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    val userData = UserData(name, email, password)
                    showError = false
                    onRegisterClick(userData)
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}