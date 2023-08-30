package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Try
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun ErrorHandler(reload: () -> Unit, errorMessage: String){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text = errorMessage, style = MaterialTheme.typography.body1)
        RoundedButton(text = stringResource(R.string.reload), onClick = reload, icon = Icons.Default.RotateLeft)
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorHandlerPreview(){
    AlgoTrackTheme() {
        Surface() {
            ErrorHandler(reload = { /*TODO*/ }, errorMessage = "test error message")
        }
    }
}