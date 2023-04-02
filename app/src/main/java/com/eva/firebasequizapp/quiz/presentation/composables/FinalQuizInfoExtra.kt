package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.eva.firebasequizapp.quiz.presentation.FullQuizViewModel
import com.eva.firebasequizapp.quiz.util.FinalQuizDialogEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalQuizInfoExtra(
    attempted:Int,
    content: List<QuestionModel?>,
    modifier: Modifier = Modifier,
    viewModel: FullQuizViewModel = hiltViewModel()
) {
    if (viewModel.routeState.value.showDialog)
        AlertDialog(
            onDismissRequest = {},
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onDialogEvent(FinalQuizDialogEvents.ContinueQuiz)
                }) { Text(text = "Continue") }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onDialogEvent(FinalQuizDialogEvents.SubmitQuiz) }
                ) {
                    Text(text = "Submit")
                }
            },
            title = {
                Text(
                    text = "Thanks for your participation ",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.quiz_submition_desc),
                    textAlign = TextAlign.Center
                )
            }
        )


    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Slider(
                value = attempted.toFloat() / content.size,
                onValueChange = {},
                steps = content.size,
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    inactiveTickColor = MaterialTheme.colorScheme.surfaceTint,
                    activeTickColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${attempted}/${content.size}",
                    letterSpacing = 2.sp,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}