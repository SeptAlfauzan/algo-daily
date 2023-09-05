package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.ui.component.AvatarProfile
import com.septalfauzan.algotrack.ui.component.AvatarProfileType
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun UserChangeProfilePicScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            AvatarProfile(
                onClick = {},
                type = AvatarProfileType.WITH_EDIT_LARGE,
                imageUri = "https://avatars.githubusercontent.com/u/48860168?v=4"
            )
            Spacer(modifier = Modifier.height(152.dp))
            Row(horizontalArrangement = Arrangement.End) {
                RoundedButton(text = "batal edit", onClick = { /*TODO*/ })
                Spacer(modifier = Modifier.width(16.dp))
                RoundedButton(text = "simpan perubahan", onClick = { /*TODO*/ })
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    AlgoTrackTheme {
        Surface {
            UserChangeProfilePicScreen()
        }
    }
}