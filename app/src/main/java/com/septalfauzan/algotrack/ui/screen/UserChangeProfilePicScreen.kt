package com.septalfauzan.algotrack.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.helper.uriToFile
import com.septalfauzan.algotrack.ui.component.AvatarProfile
import com.septalfauzan.algotrack.ui.component.AvatarProfileType
import com.septalfauzan.algotrack.ui.component.BottomSheetErrorHandler
import com.septalfauzan.algotrack.ui.component.RoundedButton
import kotlinx.coroutines.flow.StateFlow
import java.io.File

@Composable
fun UserChangeProfilePicScreen(
    profileStateFlow: StateFlow<UiState<GetProfileResponse>>,
    getProfile: () -> Unit,
    reloadProfile: () -> Unit,
    updatePP: (File?) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var imageFile by remember { mutableStateOf<File?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageFile = uriToFile(context, uri)
        }
    }

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
                            onClick = { launcher.launch("image/*") },
                            type = AvatarProfileType.WITH_EDIT_LARGE,
                            imageUri = response.data.photoUrl ?: ""
                        )
                        Spacer(modifier = Modifier.height(152.dp))
                        Row(horizontalArrangement = Arrangement.End) {
                            RoundedButton(text = "batal edit", onClick = {
                                updatePP(null)
                                navController.popBackStack()
                            })
                            Spacer(modifier = Modifier.width(16.dp))
                            RoundedButton(text = "simpan perubahan", onClick = {
                                updatePP(imageFile)
                                navController.popBackStack()
                            })
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