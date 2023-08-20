package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

enum class AvatarProfileType {
    NORMAL,
    WITH_EDIT
}

@Composable
fun AvatarProfile(
    type: AvatarProfileType = AvatarProfileType.NORMAL,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(58.dp)
    ) {
        AsyncImage(
            model = "https://avatars.githubusercontent.com/u/48860168?v=4",
            contentDescription = "avatar profile",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}