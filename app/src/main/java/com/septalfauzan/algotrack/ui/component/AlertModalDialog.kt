package com.septalfauzan.algotrack.ui.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
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
                title = stringResource(id = R.string.do_want_leave),
                text = stringResource(id = R.string.confirm_leave)
            )
        }
    }
}