package com.septalfauzan.algotrack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.ui.component.LogRegButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(bottom = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header()
        Text(
            text = stringResource(R.string.login_form_title),
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.size(8.dp))
        LoginForm()
    }
}

@Composable
private fun Header(){
    Box(
        Modifier
            .fillMaxWidth()
            .height(262.dp)
            .clip(RoundedCornerShape(bottomStart = 88.dp))
            .background(MaterialTheme.colors.secondary)
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_illustration),
            contentDescription = "login illustration",
            modifier = Modifier
                .align(Alignment.Center)
                .width(240.dp)
                .height(176.dp)
        )
    }
}

@Composable
private fun LoginForm(){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 76.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RoundedTextInput(
            label = "Email",
            onChange = { email = it },
            value = email,
            keyboardType = KeyboardType.Email,
            modifier = Modifier.fillMaxWidth()
        )
        RoundedTextInput(
            label = "Password",
            onChange = { password = it },
            value = password,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Row() {
            Text(text = "Dont have account, ")
            Text(
                text = "Register",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable { /* go to register screen */ })
        }
        LogRegButton(text = "login", onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    AlgoTrackTheme {
        LoginScreen()
    }
}