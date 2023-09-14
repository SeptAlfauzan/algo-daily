package com.septalfauzan.algotrack.ui.component

import com.septalfauzan.algotrack.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun VacationBanner(action: () -> Unit, isWork: Boolean = true, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(136.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
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
                    text = if(isWork) stringResource(R.string.ask_vacation) else stringResource(id = R.string.do_want_leave),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )
                RoundedButton(
                    text = if(isWork) stringResource(R.string.create_vacation) else stringResource(
                        id = R.string.change_status
                    ),
                    onClick = action,
                    buttonType = ButtonType.SECONDARY,
                    modifier = Modifier.align(if(!isWork) Alignment.End else Alignment.Start)
                )
            }
            if(isWork){
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
}

@Preview(showBackground = true)
@Composable
private fun preview(){
    AlgoTrackTheme() {
        Surface() {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                VacationBanner(action = { /*TODO*/ }, isWork = true)
                VacationBanner(action = { /*TODO*/ }, isWork = false)
            }
        }
    }
}