package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.UserChangePassword
import com.septalfauzan.algotrack.ui.component.AlertModalDialog
import com.septalfauzan.algotrack.ui.component.BottomSheetErrorHandler
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.component.RoundedTextInput
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    eventMessage: Flow<MyEvent>,
    changePassword : (UserChangePassword) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val newPassword = UserChangePassword(password, passwordConfirm)
    var errorMessage: String? by remember{ mutableStateOf(null) }

    LaunchedEffect(Unit){
        eventMessage.collect { event ->
            when(event) {
                is MyEvent.MessageEvent -> errorMessage = event.message
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = { Text(text = stringResource(id = R.string.change_password)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp
                )
            },
        ) { paddingValues ->
            Column(
                modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.save),
                    onClick = { showDialog = true },
                )
                AlertModalDialog(isShowed = showDialog,
                    title = stringResource(R.string.change_pw_question),
                    text = stringResource(
                        R.string.change_pw_question_desc
                    ),
                    onStateChange = {
                        showDialog = it
                        changePassword(newPassword)
                    }
                )
            }
            errorMessage?.let{msg ->
                BottomSheetErrorHandler(message = msg, dismissLabel = stringResource(R.string.closed), action = {
                    errorMessage = null
                })
            }
        }
    }
}