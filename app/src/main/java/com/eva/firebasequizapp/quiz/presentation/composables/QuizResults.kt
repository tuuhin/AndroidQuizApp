package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel
import com.eva.firebasequizapp.quiz.presentation.HomeScreenViewModel
import com.eva.firebasequizapp.quiz.util.DeleteQuizResultsEvent
import com.eva.firebasequizapp.quiz.util.DeleteQuizResultsState

@Composable
fun QuizResults(
    content: List<QuizResultModel?>,
    state:DeleteQuizResultsState,
    onDeleteConfirm: () -> Unit,
    onDeleteCancelled: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    if (state.isDialogOpen)
        AlertDialog(
            onDismissRequest = onDeleteCancelled,
            confirmButton = {
                Button(
                    onClick = onDeleteConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = "Delete")
                }
            }, dismissButton = {
                TextButton(
                    onClick = onDeleteCancelled,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "Delete Result", style = MaterialTheme.typography.titleLarge) },
            text = {
                val quizName = state.result?.quiz?.subject ?: ""
                Text(text = "Are you sure you wanna delete the result for the quiz $quizName")
            },
            titleContentColor = MaterialTheme.colorScheme.error
        )
    Text(
        text = buildAnnotatedString {
            append("You have participated in ")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append("${content.size}")
            }
            append(" quizzes")
        },
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    )
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        itemsIndexed(content) { _, item ->
            item?.let { model ->
                QuizResultsCard(
                    result = model,
                    onDelete = {
                        viewModel
                            .onDeleteResult(DeleteQuizResultsEvent.ResultsSelected(model))
                    }
                )
            }
        }
    }
}