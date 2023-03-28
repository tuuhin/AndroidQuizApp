package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.quiz.data.parcelable.toParcelable
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.presentation.AllQuizzesViewModel
import com.eva.firebasequizapp.quiz.util.QuizArrangementStyle
import com.eva.firebasequizapp.quiz.util.QuizInteractionEvents

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllQuizList(
    quizzes: List<QuizModel?>,
    navController: NavController,
    modifier: Modifier = Modifier,
    arrangementStyle: QuizArrangementStyle = QuizArrangementStyle.GridStyle,
    viewModel: AllQuizzesViewModel = hiltViewModel()
) {
    val showDialog = viewModel.showDialog.value
    val selectedQuiz = viewModel.dialogContent.value

    if (showDialog && selectedQuiz != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(QuizInteractionEvents.QuizUnselect) },
            confirmButton = {
                Button(onClick = {
                    viewModel.onEvent(QuizInteractionEvents.QuizUnselect)
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        NavParams.QUIZ_TAG,
                        selectedQuiz.toParcelable()
                    )
                    navController.navigate(NavRoutes.NavQuizRoute.route + "/${selectedQuiz.uid}")
                }) {
                    Text(text = "Start")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(QuizInteractionEvents.QuizUnselect) }) {
                    Text(text = "Cancel", color = MaterialTheme.colorScheme.secondary)
                }
            },
            title = { Text(text = selectedQuiz.subject) },
            text = { Text(text = "Start this quiz") },
        )
    }

    when (arrangementStyle) {
        QuizArrangementStyle.GridStyle -> {
            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = StaggeredGridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(quizzes.size) { index ->
                    val currentQuiz = quizzes[index]
                    currentQuiz?.let { quiz ->
                        QuizCard(quiz = quiz, arrangement = arrangementStyle, onClick = {
                            viewModel.onEvent(QuizInteractionEvents.QuizSelected(quiz))
                        })
                    }
                }
            }
        }
        QuizArrangementStyle.ListStyle -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(quizzes.size) { index ->
                    val currentQuiz = quizzes[index]
                    currentQuiz?.let { quiz ->
                        QuizCard(quiz = quiz, arrangement = arrangementStyle, onClick = {
                            viewModel.onEvent(QuizInteractionEvents.QuizSelected(quiz))
                        })
                    }
                }
            }
        }
    }
}