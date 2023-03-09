package com.eva.firebasequizapp.quiz.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.quiz.presentation.AllQuizzesViewModel
import com.eva.firebasequizapp.quiz.presentation.QuizInteractionEvents
import com.eva.firebasequizapp.quiz.presentation.composables.QuizCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllQuizzesScreen(
    modifier: Modifier = Modifier,
    viewModel: AllQuizzesViewModel = hiltViewModel(),
) {
    val quizzes = viewModel.quizzes.value
    val showDialog = viewModel.showDialog.value
    val selectedQuiz = viewModel.dialogContent.value

    if (showDialog && selectedQuiz != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(QuizInteractionEvents.QuizUnselect) },
            confirmButton = {
                Button(onClick = {}) {
                    Text(text = "OK start the quiz")
                }
            },
            title = { Text(text = selectedQuiz.subject) },
            text = { Text(text = "Start this quiz") },
        )
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.padding(4.dp)
    ) {
        items(quizzes.size) { index ->
            val currentQuiz = quizzes[index]
            currentQuiz?.let { quiz ->
                QuizCard(
                    quiz = quiz,
                    onClick = {
                        viewModel.onEvent(QuizInteractionEvents.QuizSelected(currentQuiz))
                    }
                )
            }
        }
    }
}