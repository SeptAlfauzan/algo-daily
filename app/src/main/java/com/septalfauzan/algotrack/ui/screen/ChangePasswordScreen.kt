package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.ui.component.AlertModalDialog
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChangePasswordScreen(modifier: Modifier = Modifier) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.change_password),
            modifier = Modifier
                .width(240.dp)
                .height(178.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.change_pw_screen_img_desc)
        )
        RoundedTextInput(
            icon = Icons.Default.Key,
            label = stringResource(id = R.string.password),
            onChange = { password = it },
            value = password,
            keyboardType = KeyboardType.Password,
            keyboardAction = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            withOnBlur = true,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        RoundedTextInput(
            icon = Icons.Default.Key,
            label = stringResource(R.string.confirm_password),
            onChange = { passwordConfirm = it },
            value = passwordConfirm,
            keyboardType = KeyboardType.Password,
            keyboardAction = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            withOnBlur = true,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(72.dp))
        RoundedButton(
            text = stringResource(R.string.save),
            onClick = { showDialog = true },
            modifier = Modifier.width(148.dp)
        )
        AlertModalDialog(isShowed = showDialog,
            title = stringResource(R.string.change_pw_question),
            text = stringResource(
                R.string.change_pw_question_desc
            ),
            onStateChange = { showDialog = it })
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    AlgoTrackTheme() {
        Surface {
            ChangePasswordScreen()
        }
    }
}