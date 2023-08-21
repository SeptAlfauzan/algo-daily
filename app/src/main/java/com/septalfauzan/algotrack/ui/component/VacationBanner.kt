package com.septalfauzan.algotrack.ui.component

import com.septalfauzan.algotrack.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VacationBanner(onCreateVacation: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(136.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp)) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.ask_vacation),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )
                RoundedButton(
                    text = stringResource(R.string.create_vacation),
                    onClick = onCreateVacation,
                    buttonType = ButtonType.SECONDARY
                )
            }
            Image(
                painter = painterResource(id = R.drawable.vacation),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
    }
}