package com.septalfauzan.algotrack.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.ui.component.AvatarProfile
import com.septalfauzan.algotrack.ui.component.AvatarProfileType
import com.septalfauzan.algotrack.ui.component.SwitchButton
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.utils.bottomBorder

@Composable
fun ProfileScreen(
    userId: String,
    logout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 42.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AvatarProfile(onClick = { /*edit view*/ }, type = AvatarProfileType.WITH_EDIT)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Nama", style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight(300)
                    )
                )
                Text(
                    text = "Username", style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight(300)
                    )
                )
                Text(
                    text = "user@email.com", style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight(300)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
        SettingMenu(logout = logout)
    }
}

@Composable
private fun SettingMenu(logout: () -> Unit, modifier: Modifier = Modifier) {
    var onDuty by rememberSaveable { mutableStateOf(true) }
    var notification by rememberSaveable { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                isChecked = notification,
                onClick = { notification = !notification })
        }
        SettingItem(text = "Bahasa", icon = Icons.Default.Language) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
        SettingItem(text = "Ganti password", icon = Icons.Default.Key) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
        SettingItem(text = "Logout", icon = Icons.Default.Logout, onClick = logout) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
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
            ProfileScreen(userId = "1", logout = {})
        }
    }
}