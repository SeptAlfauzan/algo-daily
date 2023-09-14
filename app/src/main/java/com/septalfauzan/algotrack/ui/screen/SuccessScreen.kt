package com.septalfauzan.algotrack.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
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
        LottieAnimation(
            composition = composition,
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 64.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title ?: "Absen berhasil dikirim",
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight(700)
                ),
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = desc ?: "Terimakasih telah \nmengisi absen",
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
                text = "Home",
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