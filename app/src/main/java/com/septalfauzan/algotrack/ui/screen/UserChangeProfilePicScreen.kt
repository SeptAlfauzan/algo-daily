package com.septalfauzan.algotrack.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.data.event.MyEvent
import com.septalfauzan.algotrack.domain.model.ui.UiState
import com.septalfauzan.algotrack.data.source.remote.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.ui.component.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@Composable
fun UserChangeProfilePicScreen(
    profileStateFlow: StateFlow<UiState<GetProfileResponse>>,
    getProfile: () -> Unit,
    reloadProfile: () -> Unit,
    updatePP: (File?) -> Unit,
    navController: NavController,
    eventMessage: Flow<MyEvent>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var imageFile by remember { mutableStateOf<File?>(null) }
    var errorMessage: String? by remember{ mutableStateOf(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageFile = convertContentUriToImageFile(context, uri)
            }
        }
    LaunchedEffect(Unit){
        eventMessage.collect { event ->
            when(event) {
                is MyEvent.MessageEvent -> errorMessage = event.message
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(text = stringResource(id = R.string.change_pp_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
    ) {paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()) {
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
                                imageUri = if (imageFile != null) imageFile!!.path else response.data.photoUrl ?: ""
                            )
                            Spacer(modifier = Modifier.height(152.dp))
                            Row(horizontalArrangement = Arrangement.End) {
                                RoundedButton(
                                    text = stringResource(id = R.string.cancel_change),
                                    buttonType = ButtonType.SECONDARY,
                                    enabled = imageFile?.isFile ?: false,
                                    onClick = {
                                        navController.popBackStack()
                                    })
                                Spacer(modifier = Modifier.width(16.dp))
                                RoundedButton(
                                    text = stringResource(id = R.string.save_change),
                                    enabled = imageFile?.isFile ?: false,
                                    onClick = {
                                        updatePP(imageFile)
                                    })
                            }
                        }
                    }
                    is UiState.Error -> {
                        BottomSheetErrorHandler(message = uiState.errorMessage, action = reloadProfile)
                    }
                }
            }

            errorMessage?.let{msg ->
                BottomSheetErrorHandler(message = msg, dismissLabel = stringResource(R.string.closed), action = {
                    errorMessage = null
                })
            }
        }
    }
}

private fun convertContentUriToImageFile(context: Context, contentUri: Uri): File? {
    val contentResolver: ContentResolver = context.contentResolver
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null
    var imageFile: File? = null

    try {
        // Open an input stream from the content URI
        inputStream = contentResolver.openInputStream(contentUri)

        if (inputStream != null) {
            // Create a temporary file to store the image
            val tempDir = context.cacheDir
            imageFile = File.createTempFile("image_", ".jpg", tempDir)

            // Open an output stream to the temporary file
            outputStream = FileOutputStream(imageFile)

            // Read from the input stream and write to the output stream
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            // Close the input and output streams
            inputStream?.close()
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return imageFile
}