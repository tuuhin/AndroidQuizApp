package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizEvents
import com.eva.firebasequizapp.contribute_quiz.presentation.QuizViewModel

@Composable
fun QuizImagePicker(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val image = viewModel.createQuiz.value.image
    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.onCreateQuizEvent(CreateQuizEvents.OnImageAdded(uri))
        }
    Column(
        modifier = Modifier.padding(PaddingValues(top = 8.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.9f)
            ) {
                Text(
                    text = "Pick image",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(id = R.string.image_extra),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(2.dp)
                )
            }
            IconButton(
                onClick = { viewModel.onCreateQuizEvent(CreateQuizEvents.OnImageRemoved) },
                modifier = Modifier.weight(0.1f)
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircleOutline,
                    contentDescription = "Remove added image"
                )
            }
        }
        Box(
            modifier = modifier
                .aspectRatio(1.5f)
                .padding(2.dp)
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = Color.Gray,
                        style = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        ),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                }
                .clickable {
                    if (image == null) {
                        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                }, contentAlignment = Alignment.Center
        ) {
            if (image != null)
                AsyncImage(
                    model = ImageRequest.Builder(context).data(image).build(),
                    contentDescription = "User photo",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .aspectRatio(1.5f)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                )
            else
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = "Image",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                    Text(
                        text = "Aspect Ratio = 3:2",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

        }
    }
}