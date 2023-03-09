package com.eva.firebasequizapp.profile.presentation.composables

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eva.firebasequizapp.profile.presentation.UserProfileViewModel

@Composable
fun FireBaseUserTopBar(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val user = viewModel.user
    SmallTopAppBar(
        modifier = modifier.padding(10.dp, 0.dp),
        title = { (if (user?.displayName != null) user.displayName else "Blank")?.let { Text(text = it) } },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                if (user?.photoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(user.photoUrl)
                            .build(),
                        contentDescription = "User photo",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User photo placeholder",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

    )

}