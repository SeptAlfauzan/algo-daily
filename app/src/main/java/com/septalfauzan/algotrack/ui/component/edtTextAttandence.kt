package com.septalfauzan.algotrack.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.theme.RedAccent

@Composable
fun EdtTextAttandence(
    label: String,
    onChange: (String) -> Unit,
    value: String,
    error: Boolean = false,
    errorText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardAction: KeyboardActions = KeyboardActions.Default,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
){
    Column {
        if(error) Text(text = errorText, style = MaterialTheme.typography.caption.copy(
            color = RedAccent
        ))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            OutlinedTextField(
                isError = error,
                label = { Text(text = label, style = MaterialTheme.typography.caption) },
                value = value,
                onValueChange = onChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    textColor = Color.Black,
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(4.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardAction,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    var value by remember { mutableStateOf("") }

    AlgoTrackTheme {
        Surface {
            EdtTextAttandence(label = "Ketik Disini", onChange = { value = it }, value = value)
        }
    }
}