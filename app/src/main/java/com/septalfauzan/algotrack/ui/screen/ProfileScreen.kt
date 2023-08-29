package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.component.*
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.utils.bottomBorder
import com.septalfauzan.algotrack.ui.utils.shimmer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProfileScreen(
    logout: () -> Unit,
    toggleTheme: () -> Unit,
    isDarkMode: Boolean,
    isNotificationReminderActive: Boolean,
    setNotificationReminder: () -> Unit,
    navController: NavHostController,
    cancelNotificationReminder: () -> Unit,
    profileUiState: StateFlow<UiState<GetProfileResponse>>,
    getProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                            onClick = { /*edit view*/ },
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
                is UiState.Error -> {
                    Text("error: ${uiData.errorMessage}")
                }
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
        SettingMenu(
            logout = logout,
            toggleTheme = toggleTheme,
            isDarkMode = isDarkMode,
            setNoficationReminder = setNotificationReminder,
            cancelNotificationReminder = cancelNotificationReminder,
            navController = navController,
            isNotificationReminderActive = isNotificationReminderActive
        )
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
    setNoficationReminder: () -> Unit,
    cancelNotificationReminder: () -> Unit,
    isNotificationReminderActive: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var onDuty by rememberSaveable { mutableStateOf(true) }
    var notification by rememberSaveable { mutableStateOf(true) }
    var logoutAlertShowed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingItem(
            text = "On duty",
            icon = Icons.Default.Work,
            modifier = Modifier.bottomBorder(
                0.5.dp,
                MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        ) {
            SwitchButton(
                isChecked = onDuty,
                onClick = { onDuty = !onDuty })
        }
        Text(
            stringResource(R.string.setting), style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight(300),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        )
        SettingItem(text = "Notifikasi", icon = Icons.Default.NotificationsNone) {
            SwitchButton(
                isChecked = isNotificationReminderActive,
                onClick = {
                    when (isNotificationReminderActive) {
                        true -> cancelNotificationReminder()
                        else -> setNoficationReminder()
                    }
                })
        }
        SettingItem(text = "Mode malam", icon = Icons.Default.NotificationsNone) {
            SwitchButton(
                isChecked = isDarkMode,
                onClick = toggleTheme
            )
        }
        SettingItem(text = "Bahasa", icon = Icons.Default.Language) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
        SettingItem(text = "Ganti password", icon = Icons.Default.Key, onClick = {
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
            text = "Logout",
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
            .clip(RoundedCornerShape(4.dp))
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


@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    AlgoTrackTheme {
        Surface() {
            ProfileScreen(
                logout = {},
                toggleTheme = { },
                isDarkMode = false,
                isNotificationReminderActive = false,
                setNotificationReminder = { },
                navController = rememberNavController(),
                cancelNotificationReminder = {},
                getProfile = {},
                profileUiState = MutableStateFlow(UiState.Loading),
            )
        }
    }
}