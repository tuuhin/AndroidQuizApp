package com.eva.firebasequizapp.profile.presentation.composables

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.eva.firebasequizapp.profile.presentation.ChangeImageEvents
import com.eva.firebasequizapp.profile.presentation.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProfileImage(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val imageOption = remember {
        CropImageOptions(
            guidelines = CropImageView.Guidelines.ON,
            aspectRatioX = 1,
            aspectRatioY = 1,
            scaleType = CropImageView.ScaleType.CENTER,
            fixAspectRatio = true,
            outputCompressQuality = 70
        )
    }

    val user = viewModel.user
    val state = viewModel.profileImageState.value
    val uri = viewModel.profileImageState.value.uri

    val cropper = rememberLauncherForActivityResult(CropImageContract()) {
        val content = it.uriContent
        if (content != null)
            viewModel.onProfileChangeEvent(ChangeImageEvents.PickImage(content))
    }
    val imagePicker = rememberLauncherForActivityResult(
        PickVisualMedia()
    ) { image ->
        cropper.launch(CropImageContractOptions(image, imageOption))
    }

    if (state.isDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                if (state.isDismissAllowed) viewModel.onProfileChangeEvent(
                    ChangeImageEvents.ToggleDialog
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onProfileChangeEvent(ChangeImageEvents.SubmitChanges)
                }) {
                    Text(text = "Change")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onProfileChangeEvent(ChangeImageEvents.ToggleDialog) }) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "Are you Ok with this image") },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    uri?.let {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(uri).build(),
                            contentDescription = "New Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            })
    }
    Card(
        modifier = modifier.padding(PaddingValues(top = 4.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(.75f)
            ) {
                Text(text = "Change image", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Wanna change the profile picture",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            TextButton(
                onClick = {
                    imagePicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                },
                modifier = Modifier.weight(.25f)
            ) {
                Text(text = if (user?.displayName != null) "Change" else "Set")
            }
        }
    }
}