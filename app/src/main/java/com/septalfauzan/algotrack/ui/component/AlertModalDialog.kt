package com.septalfauzan.algotrack.ui.component

import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

/**
 * @param isShowed is to determined whether alert is show or not
 * @param title is used to determined title text content
 * @param text is used to determined description content text of the alert modal
 * @param onStateChange is callback to trigger what behaviour Alert Model should do when user click action such as onDismiss, onClick confirm, or onClick dismiss.
 */
@Composable
fun AlertModalDialog(
    isShowed: Boolean,
    title: String,
    text: String,
    onStateChange: (Boolean) -> Unit,
    onConfirmYes: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (isShowed) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onStateChange(false) },
            title = { Text(title, style = MaterialTheme.typography.h6) },
            text = { Text(text, color = MaterialTheme.colors.onSurface.copy(0.3f)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onStateChange(false)
                        onConfirmYes()
                    }
                ){
                    Text(text = stringResource(R.string.yes), color = MaterialTheme.colors.primary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onStateChange(false) },
                ){
                    Text(text = stringResource(R.string.no), color = MaterialTheme.colors.secondary)
                }
            },
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    var showDialog by remember {
        mutableStateOf(false)
    }
    AlgoTrackTheme() {
        Surface() {
            TextButton(onClick = { showDialog = true }) { Text(text = "show dialog") }
            AlertModalDialog(
                isShowed = showDialog,
                onStateChange = { showDialog = it },
                title = "Apakah anda yakin ingin mengajukan cuti hari ini?",
                text = "Dengan mengajukan cuti, otomatis timer absen akan dinonaktifkan"
            )
        }
    }
}