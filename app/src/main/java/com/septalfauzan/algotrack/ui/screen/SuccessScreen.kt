package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.septalfauzan.algotrack.R
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.component.ButtonType
import com.septalfauzan.algotrack.ui.component.RoundedButton
import com.septalfauzan.algotrack.ui.theme.AlgoTrackTheme

@Composable
fun SuccessScreen(navController: NavController, title: String? = null, desc: String? = null) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_thumb))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimation(
                modifier = Modifier.fillMaxWidth().height(240.dp),
                contentScale = ContentScale.Crop,
                composition = composition,
            )
            Text(
                text = title ?: stringResource(id = R.string.absence_sent_successfully),
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight(700)
                ),
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = desc ?: stringResource(id = R.string.thank_you_message),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            RoundedButton(
                onClick = { navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Success.route) {
                        inclusive = true
                    }
                } },
                text = stringResource(id = R.string.home),
                modifier = Modifier.width(150.dp),
                buttonType = ButtonType.SECONDARY
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    AlgoTrackTheme {
        SuccessScreen(navController = rememberNavController(), title = null, desc = null)
    }
}