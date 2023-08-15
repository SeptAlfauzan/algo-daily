@file:OptIn(ExperimentalComposeUiApi::class)

package com.septalfauzan.algotrack.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.UserData
import com.septalfauzan.algotrack.ui.component.Header
import com.septalfauzan.algotrack.ui.component.LogRegButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, navController: NavController){
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
            onRegisterClick = { navController.navigate("login") },
            onLoginCLick = { navController.navigate("login") }
        )
    }
}

@Composable
private fun RegisterForm(onRegisterClick: (UserData) -> Unit, onLoginCLick: () -> Unit){

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            label = "No. Telp.",
            icon = Icons.Default.Phone,
            onChange = { phone = it },
            value = phone,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )
        RoundedTextInput(
            label = "Email",
            icon = Icons.Default.Email,
            onChange = { email = it },
            value = email,
            keyboardType = KeyboardType.Email,

imeAction = ImeAction.Next,            modifier = Modifier.fillMaxWidth()
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
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Text(text = stringResource(id = R.string.already_have_account))
            Text(
                text = stringResource(id = R.string.login),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable { onLoginCLick() })
        }
        LogRegButton(
            text = stringResource(R.string.register),
            onClick = {
                val userData = UserData(name, phone, email, password)
                onRegisterClick(userData)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}