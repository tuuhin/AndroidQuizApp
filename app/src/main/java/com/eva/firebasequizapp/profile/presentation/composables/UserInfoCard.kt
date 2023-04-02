package com.eva.firebasequizapp.profile.presentation.composables

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoCard(
    user: FirebaseUser,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    OutlinedCard(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.apply {
                    if (user.photoUrl != null)
                        weight(0.7f)
                }
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Name:")
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append(user.displayName)
                        }
                    }
                )
                Text(
                    text = buildAnnotatedString {
                        append("Email: ")
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.secondary)
                        ) {
                            append(user.email)
                        }
                    },
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = buildAnnotatedString {
                        append("Uid: ")
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.secondary)
                        ) {
                            append(user.uid)
                        }
                    },
                    style = MaterialTheme.typography.labelMedium
                )
            }
            user.photoUrl?.let { uri ->
                Spacer(modifier = Modifier.weight(.05f))
                Box(
                    modifier = Modifier
                        .weight(.25f)
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(uri).build(),
                        contentDescription = "User photo",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .size(50.dp)
                    )
                }
            }
        }
    }
}