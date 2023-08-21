package com.septalfauzan.algotrack.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.septalfauzan.algotrack.R

enum class AvatarProfileType {
    NORMAL,
    WITH_EDIT
}

/**
 * @param onClick is action when user tap the avatar
 * @param type is type to define which type AvatarProfile is used
 */
@Composable
fun AvatarProfile(
    onClick: () -> Unit,
    type: AvatarProfileType = AvatarProfileType.NORMAL,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(if(type == AvatarProfileType.NORMAL) 58.dp else 76.dp)
    ) {
        if(type == AvatarProfileType.WITH_EDIT) Icon(
            imageVector = Icons.Default.Edit,
            tint = MaterialTheme.colors.background,
            contentDescription = stringResource(R.string.edit_profile_pic),
            modifier = Modifier
                .size(26.dp)
                .clip(
                    CircleShape
                )
                .background(MaterialTheme.colors.secondary)
                .clickable {
                    onClick()
                }
                .padding(4.dp)
                .align(Alignment.BottomEnd)
                .zIndex(2f)
        )
        AsyncImage(
            model = "https://avatars.githubusercontent.com/u/48860168?v=4",
            contentDescription = "avatar profile",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .clickable { if (type == AvatarProfileType.NORMAL) onClick() },
            contentScale = ContentScale.Crop
        )
    }
}