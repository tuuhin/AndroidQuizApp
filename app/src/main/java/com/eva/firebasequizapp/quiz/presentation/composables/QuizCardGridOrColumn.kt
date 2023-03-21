package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.quiz.domain.models.QuizModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizCardGridOrColumn(
    quizzes: List<QuizModel?>,
    navController: NavController,
    modifier: Modifier = Modifier,
    arrangementStyle: QuizArrangementStyle = QuizArrangementStyle.GridStyle,
) {
    when (arrangementStyle) {
        QuizArrangementStyle.GridStyle -> {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(quizzes.size) { index ->
                    val currentQuiz = quizzes[index]
                    currentQuiz?.let { quiz ->
                        QuizCard(quiz = quiz, arrangement = arrangementStyle, onClick = {
                            navController.navigate(NavRoutes.NavAddQuestionsRoute.route + "/${quiz.uid}")
                        })
                    }
                }
            }
        }
        QuizArrangementStyle.ListStyle -> {
            LazyColumn(
                modifier = modifier.padding(PaddingValues(top = 4.dp)),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(quizzes.size) { index ->
                    val currentQuiz = quizzes[index]
                    currentQuiz?.let { quiz ->
                        QuizCard(quiz = quiz, arrangement = arrangementStyle, onClick = {
                            navController.navigate(NavRoutes.NavAddQuestionsRoute.route + "/${quiz.uid}")
                        })
                    }
                }
            }
        }
    }
}