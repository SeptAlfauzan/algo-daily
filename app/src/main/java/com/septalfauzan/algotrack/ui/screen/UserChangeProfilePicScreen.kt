package com.septalfauzan.algotrack.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.ui.component.AvatarProfile
import com.septalfauzan.algotrack.ui.component.AvatarProfileType
import com.septalfauzan.algotrack.ui.component.BottomSheetErrorHandler
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserChangeProfilePicScreen(
    profileStateFlow: StateFlow<UiState<GetProfileResponse>>,
    getProfile: () -> Unit,
    reloadProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        profileStateFlow.collectAsState(initial = UiState.Error("error")).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                    getProfile()
                }
                is UiState.Success -> {
                    val response = uiState.data
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AvatarProfile(
                            onClick = {},
                            type = AvatarProfileType.WITH_EDIT_LARGE,
                            imageUri = response.data.photoUrl ?: ""
                        )
                        Spacer(modifier = Modifier.height(152.dp))
                        Row(horizontalArrangement = Arrangement.End) {
                            RoundedButton(text = "batal edit", onClick = { /*TODO*/ })
                            Spacer(modifier = Modifier.width(16.dp))
                            RoundedButton(text = "simpan perubahan", onClick = { /*TODO*/ })
                        }
                    }
                }
                is UiState.Error -> {
                    BottomSheetErrorHandler(message = uiState.errorMessage, retry = reloadProfile)
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    AlgoTrackTheme {
        Surface {
            UserChangeProfilePicScreen(
                MutableStateFlow(UiState.Error("error")),
                getProfile = {},
                reloadProfile = {},
            )
        }
    }
}