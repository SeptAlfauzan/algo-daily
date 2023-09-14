package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.helper.formatMilliseconds
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme
import java.util.Calendar

@Composable
fun TimerBanner(timer: Long, onWork: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(158.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) {
            Image(
                painter = painterResource(id = R.drawable.timer_banner_decorator),
                contentDescription = "timer banner decoration",
                modifier = Modifier
                    .width(168.dp)
                    .height(246.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 38.dp, y = -(60.dp))
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 28.dp, horizontal = 32.dp)
            ) {
                if(onWork){
                    Text(
                        text = stringResource(R.string.attendance_text_workday),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = timer.formatMilliseconds(),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.h3.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    )
                }else{
                    Text(
                        text = stringResource(id = R.string.not_working),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = stringResource(id = R.string.please_rest),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.h5.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val timer = System.currentTimeMillis()
    val currentDate =  Calendar.getInstance()
    currentDate.timeInMillis = timer

    AlgoTrackTheme() {
        Surface(Modifier.padding(24.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TimerBanner(timer = timer, onWork = true)
                TimerBanner(timer = timer, onWork = false)
            }
        }
    }
}