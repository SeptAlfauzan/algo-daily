package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import com.septalfauzan.algotrack.ui.theme.Gray
import com.septalfauzan.algotrack.ui.theme.RedAccent

sealed class ErrorCondition {
    object NO_EMPTY : ErrorCondition()
    class MIN_LENGTH(val len: Int) : ErrorCondition()
    class MAX_LENGTH(val len: Int) : ErrorCondition()

}
/**
 * This component is use to create rounded corner editable text input
 * @param label use to show text label of text input
 * @param onChange use to handle value change in text input
 * @param value is use to handle value of text input
 * @param keyboardType is use to determined keyboard type for the text input
 */
@Composable
fun RoundedTextInput(
    label: String,
    onChange: (String) -> Unit,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.h6.copy(color = Gray))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                focusedBorderColor = MaterialTheme.colors.primary,
                errorBorderColor = RedAccent
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    var value by remember { mutableStateOf("") }

    AlgoTrackTheme() {
        Surface() {
            RoundedTextInput(label = "Lorem", onChange = { value = it }, value = value)
        }
    }
}