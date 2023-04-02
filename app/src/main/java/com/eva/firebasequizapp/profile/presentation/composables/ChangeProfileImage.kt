package com.eva.firebasequizapp.profile.presentation.composables

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.eva.firebasequizapp.profile.presentation.ChangeImageState
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProfileImage(
    user: FirebaseUser?,
    state: ChangeImageState,
    onSubmit: () -> Unit,
    onToggle: () -> Unit,
    onProfileChange: (CropImageView.CropResult) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    val imageOption = remember {
        CropImageOptions(
            imageSourceIncludeCamera = false,
            guidelines = CropImageView.Guidelines.ON,
            aspectRatioX = 1,
            aspectRatioY = 1,
            scaleType = CropImageView.ScaleType.CENTER,
            fixAspectRatio = true,
            outputCompressQuality = 70
        )
    }

    val cropper = rememberLauncherForActivityResult(CropImageContract(), onResult = onProfileChange)

    val imagePicker = rememberLauncherForActivityResult(
        PickVisualMedia()
    ) { image ->
        image?.let {
            cropper.launch(CropImageContractOptions(image, imageOption))
        }
    }

    if (state.isDialogOpen) {
        AlertDialog(
            onDismissRequest = { if (state.isDismissAllowed) onToggle() },
            confirmButton = {
                Button(
                    onClick = onSubmit
                ) { Text(text = "Change") }
            },
            dismissButton = {
                TextButton(
                    onClick = onToggle
                ) { Text(text = "Cancel") }
            },
            title = { Text(text = "Change Image") },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    state.uri?.let { imageUri ->
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(imageUri).build(),
                            contentDescription = "New Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        )
    }
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
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
                Text(
                    text = "Change image",
                    style = MaterialTheme.typography.titleMedium
                )
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