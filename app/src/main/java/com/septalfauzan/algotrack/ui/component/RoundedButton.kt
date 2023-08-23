package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

enum class ButtonType {
    PRIMARY,
    SECONDARY
}

/**
 * This composable function is used to create custom rounded button
 * @param text to display in the button content
 * @param onClick action when button is clicked
 * @param buttonType for button style type (primary or secondary) by default is primary
 * @param modifier to overwrite button compose modifier
 */
@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit,
    buttonType: ButtonType = ButtonType.PRIMARY,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = if (buttonType == ButtonType.PRIMARY) MaterialTheme.colors.primary else MaterialTheme.colors.secondary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AlgoTrackTheme() {
        Surface() {
            Column() {
                RoundedButton("Login", onClick = {}, modifier = Modifier.fillMaxWidth())
                RoundedButton("Login", onClick = {}, buttonType = ButtonType.SECONDARY)
            }
        }
    }
}
