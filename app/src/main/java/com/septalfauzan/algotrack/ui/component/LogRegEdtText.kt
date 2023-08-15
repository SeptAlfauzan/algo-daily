package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
 * @param imeAction is use to determined keyboard enter button behaviour
 */
@Composable
fun RoundedTextInput(
    icon: ImageVector,
    label: String,
    onChange: (String) -> Unit,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardAction: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    var peekPassword by remember { mutableStateOf(keyboardType != KeyboardType.Password) }

    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            OutlinedTextField(
                maxLines = 1,
                singleLine = true,
                label = { Text(text = label, style = MaterialTheme.typography.caption) },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                trailingIcon = {
                    if (keyboardType == KeyboardType.Password) {
                        Icon(
                            imageVector = if (peekPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "peek password icon",
                            modifier = Modifier.clickable { peekPassword = !peekPassword })
                    }
                },
                value = value,
                onValueChange = onChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colors.secondary,
                    focusedBorderColor = MaterialTheme.colors.primary,
                    errorBorderColor = RedAccent
                ),
                modifier = modifier.height(60.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                visualTransformation = if (peekPassword) VisualTransformation.None else PasswordVisualTransformation(),
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
            RoundedTextInput(
                icon = Icons.Default.Email,
                label = "Email",
                onChange = { value = it },
                value = value
            )
        }
    }
}