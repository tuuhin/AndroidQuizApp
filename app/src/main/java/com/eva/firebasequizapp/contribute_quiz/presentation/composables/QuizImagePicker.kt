package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizEvents
import com.eva.firebasequizapp.contribute_quiz.presentation.QuizViewModel

@Composable
fun QuizImagePicker(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {

    val image = viewModel.createQuiz.value.image
    val imagepicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.onCreateQuizEvent(CreateQuizEvents.OnImageAdded(uri))
        }
    Column {

        Text(text = "Pick image", color = MaterialTheme.colorScheme.secondary)
        Box(
            modifier = modifier
                .aspectRatio(2f)
                .padding(2.dp)
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = Color.Gray, style = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        ), cornerRadius = CornerRadius(10f, 10f)
                    )
                }
                .clickable {
                    imagepicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }, contentAlignment = Alignment.Center
        ) {
            if (image != null)
                AsyncImage(
                    model = ImageRequest.Builder(context).data(image).build(),
                    contentDescription = "User photo",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .aspectRatio(2f)
                        .padding(4.dp)
                )
            else
                Icon(imageVector = Icons.Default.BrokenImage, contentDescription = "Image")
        }
    }
}