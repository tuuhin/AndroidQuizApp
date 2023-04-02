package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.quiz.data.parcelable.toParcelable
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.util.QuizArrangementStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContributedQuizList(
    quizzes: List<QuizModel?>,
    navController: NavController,
    modifier: Modifier = Modifier,
    style: QuizArrangementStyle = QuizArrangementStyle.GridStyle,
) {
    when (style) {
        QuizArrangementStyle.GridStyle -> {
            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = StaggeredGridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(quizzes.size) { index ->
                    quizzes[index]?.let { quiz ->
                        QuizCard(
                            quiz = quiz,
                            arrangement = style,
                            onClick = {
                                navController
                                    .currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(NavParams.QUIZ_TAG, quiz.toParcelable())
                                navController.navigate(NavRoutes.NavViewQuestions.route + "/${quiz.uid}")
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
                            arrangement = style,
                            onClick = {
                                navController
                                    .currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(NavParams.QUIZ_TAG, quiz.toParcelable())
                                navController.navigate(NavRoutes.NavViewQuestions.route + "/${quiz.uid}")
                            }
                        )
                    }
                }
            }
        }
    }
}