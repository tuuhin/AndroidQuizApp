package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.R
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
    showDialog: Boolean,
    onUnselect: () -> Unit,
    modifier: Modifier = Modifier,
    arrangementStyle: QuizArrangementStyle = QuizArrangementStyle.GridStyle,
    selectedQuiz: QuizModel? = null,
    viewModel: AllQuizzesViewModel = hiltViewModel()
) {

    if (showDialog && selectedQuiz != null) {
        AlertDialog(
            onDismissRequest = onUnselect,
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(QuizInteractionEvents.QuizUnselect)
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            NavParams.QUIZ_TAG,
                            selectedQuiz.toParcelable()
                        )
                        navController.navigate(NavRoutes.NavQuizRoute.route + "/${selectedQuiz.uid}")
                    }
                ) {
                    Text(text = "Start")
                }
            },
            dismissButton = {
                TextButton(onClick = onUnselect) {
                    Text(text = "Cancel", color = MaterialTheme.colorScheme.secondary)
                }
            },
            title = { Text(text = selectedQuiz.subject) },
            text = {
                Text(
                    text = stringResource(id = R.string.start_quiz_info),
                    color = MaterialTheme.colorScheme.secondary
                )
            },
        )
    }

    when (arrangementStyle) {
        QuizArrangementStyle.GridStyle -> {
            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(quizzes.size) { index ->
                    quizzes[index]?.let { quiz ->
                        QuizCard(
                            quiz = quiz,
                            arrangement = arrangementStyle,
                            onClick = {
                                viewModel.onEvent(QuizInteractionEvents.QuizSelected(quiz))
                            }
                        )
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
                    quizzes[index]?.let { quiz ->
                        QuizCard(
                            quiz = quiz,
                            arrangement = arrangementStyle,
                            onClick = {
                                viewModel.onEvent(QuizInteractionEvents.QuizSelected(quiz))
                            }
                        )
                    }
                }
            }
        }
    }
}