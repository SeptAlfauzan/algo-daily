package com.septalfauzan.algotrack.ui.screen

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.model.ui.UiState
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.component.AlertModalDialog
import com.septalfauzan.algotrack.ui.component.AvatarProfile
import com.septalfauzan.algotrack.ui.component.AvatarProfileSize
import com.septalfauzan.algotrack.ui.component.AvatarProfileType
import com.septalfauzan.algotrack.ui.component.BottomSheetErrorHandler
import com.septalfauzan.algotrack.ui.component.ErrorHandler
import com.septalfauzan.algotrack.ui.component.SwitchButton
import com.septalfauzan.algotrack.ui.utils.bottomBorder
import com.septalfauzan.algotrack.ui.utils.shimmer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProfileScreen(
    logout: () -> Unit,
    toggleTheme: () -> Unit,
    isDarkMode: Boolean,
    navController: NavHostController,
    profileUiState: StateFlow<UiState<GetProfileResponse>>,
    getProfile: () -> Unit,
    reloadProfile: () -> Unit,
    eventMessage: Flow<MyEvent>,
    setOnDuty: (Boolean) -> Unit,
    onDutyState: StateFlow<Boolean>,
    modifier: Modifier = Modifier,
) {

    var errorMessage: String? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        eventMessage.collect { event ->
            when (event) {
                is MyEvent.MessageEvent -> errorMessage = event.message
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Column(
            modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile),
                        style = MaterialTheme.typography.h6,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )
            Column(
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 42.dp)
            ) {
                profileUiState.collectAsState(initial = UiState.Loading).value.let { uiData ->
                    when (uiData) {
                        is UiState.Loading -> {
                            ShimmerLoading()
                            getProfile()
                        }
                        is UiState.Success -> {
                            val result = uiData.data
                            Row(modifier = Modifier.fillMaxWidth()) {
                                AvatarProfile(
                                    imageUri = result.data.photoUrl ?: "",
                                    onClick = { navController.navigate(Screen.UploadProfilePic.route) },
                                    type = AvatarProfileType.WITH_EDIT
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = result.data?.name ?: "",
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            fontWeight = FontWeight(600)
                                        )
                                    )
                                    Text(
                                        text = result.data?.email ?: "noemail@email.com",
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            fontWeight = FontWeight(300)
                                        )
                                    )
                                }
                            }
                        }
                        is UiState.Error -> ErrorHandler(
                            reload = reloadProfile,
                            errorMessage = "error: ${uiData.errorMessage}"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(36.dp))
                SettingMenu(
                    logout = logout,
                    toggleTheme = toggleTheme,
                    isDarkMode = isDarkMode,
                    onDutyStatusState = onDutyState,
                    setOnDuty = setOnDuty,
                    navController = navController,
                )
            }
        }
        errorMessage?.let {
            BottomSheetErrorHandler(message = it, action = { errorMessage = null })
        }
    }
}

@Composable
private fun ShimmerLoading() {
    Row(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(AvatarProfileSize.large)
                .clip(
                    CircleShape
                )
                .shimmer(true)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(16.dp)
                    .shimmer(showShimmer = true)
            )
            Box(
                modifier = Modifier
                    .width(124.dp)
                    .height(16.dp)
                    .shimmer(showShimmer = true)
            )
        }
    }
}

@Composable
private fun SettingMenu(
    logout: () -> Unit,
    isDarkMode: Boolean,
    toggleTheme: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onDutyStatusState: StateFlow<Boolean>,
    setOnDuty: (Boolean) -> Unit,
) {
    var logoutAlertShowed by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingItem(
            text = stringResource(id = R.string.on_duty_button),
            icon = Icons.Default.Work,
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .bottomBorder(
                    0.5.dp,
                    MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                )
        ) {
            SwitchButton(
                isChecked = onDutyStatusState.collectAsState().value,
                onClick = { setOnDuty(!onDutyStatusState.value) }
            )
        }
        Text(
            stringResource(R.string.setting), style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight(300),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        )
        SettingItem(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            text = stringResource(R.string.dark_theme),
            icon = Icons.Default.DarkMode
        ) {
            SwitchButton(
                isChecked = isDarkMode,
                onClick = toggleTheme
            )
        }
        SettingItem(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            text = stringResource(id = R.string.language),
            icon = Icons.Default.Language,
            onClick = {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
        SettingItem(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            text = stringResource(id = R.string.change_password),
            icon = Icons.Default.Key,
            onClick = {
                navController.navigate(
                    Screen.ChangePassword.route
                )
            }) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
        SettingItem(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            text = stringResource(id = R.string.logout),
            icon = Icons.Default.Logout,
            onClick = { logoutAlertShowed = true }) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
        AlertModalDialog(
            isShowed = logoutAlertShowed,
            title = stringResource(R.string.logout_alert_question),
            text = stringResource(
                R.string.logout_alert_desc
            ),
            onStateChange = { logoutAlertShowed = it },
            onConfirmYes = logout
        )
    }
}

/**
 * @param text is string used to display item name.
 * @param icon is used to display icon in left before of text.
 * @param onClick is used to give click action when user tap the row of item.
 * @param modifier (optional) is used to give Modifier to custom this SettingItem composable.
 * @param composableAction (optional) is to add another composable component after text, placed on end of row.
 */
@Composable
private fun SettingItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    composableAction: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.icon_desc, text.toLowerCase())
        )
        Text(
            text = text, style = MaterialTheme.typography.body1, modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        )
        composableAction()
    }
}