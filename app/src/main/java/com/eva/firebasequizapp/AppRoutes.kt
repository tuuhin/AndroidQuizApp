package com.eva.firebasequizapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eva.firebasequizapp.contribute_quiz.presentation.*
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable
import com.eva.firebasequizapp.quiz.presentation.CurrentQuizRoute
import com.eva.firebasequizapp.quiz.presentation.FullQuizViewModel
import com.eva.firebasequizapp.quiz.presentation.QuizRoute

@Composable
fun AppRoutes(
    modifier: Modifier = Modifier
) {
    val navHost = rememberNavController()
    NavHost(
        navController = navHost,
        modifier = modifier,
        startDestination = NavRoutes.NavHomeRoute.route
    ) {
        composable(NavRoutes.NavHomeRoute.route) {
            QuizRoute(navController = navHost)
        }
        composable(NavRoutes.NavCreateQuizRoute.route) {
            val viewModel = hiltViewModel<QuizViewModel>()
            CreateQuiz(
                navController = navHost,
                state = viewModel.createQuiz.value,
                showDialog = viewModel.quizDialogState.value
            )
        }
        composable(
            NavRoutes.NavQuizRoute.route + NavParams.QUIZ_PARAM_ID + NavParams.SOURCE_VALID_PARAMS_ID,
            arguments = listOf(
                navArgument(NavParams.QUIZ_ID) { type = NavType.StringType },
                navArgument(NavParams.SOURCE_VALID_ID) {
                    type = NavType.BoolType; defaultValue = true
                }
            )
        ) {
            val parcelable = navHost
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<QuizParcelable>(NavParams.QUIZ_TAG)

            val viewModel = hiltViewModel<FullQuizViewModel>()

            CurrentQuizRoute(
                navController = navHost,
                parcelable = parcelable,
                isBackHandlerEnabled = viewModel.routeState.value.isBackNotAllowed,
                fullQuizState = viewModel.fullQuizState.value
            )
        }
        composable(
            NavRoutes.NavViewQuestions.route + NavParams.QUIZ_PARAM_ID,
            arguments = listOf(navArgument(NavParams.QUIZ_ID) {
                type = NavType.StringType
            })
        ) { navStack ->
            val quizId =
                navStack.arguments?.getString(NavParams.QUIZ_ID) ?: ""
            val quiz =
                navHost
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<QuizParcelable>(NavParams.QUIZ_TAG)
            val viewModel = hiltViewModel<QuestionsViewModel>()

            ContributedQuestions(
                navController = navHost,
                quizId = quizId,
                parcelable = quiz,
                deleteQuizState = viewModel.deleteQuizState.value,
                deleteQuestionState = viewModel.deleteQuestionState.value,
                questionsState = viewModel.questions.value
            )
        }
        composable(
            NavRoutes.NavAddQuestionsRoute.route + NavParams.QUIZ_PARAM_ID,
            arguments = listOf(navArgument(NavParams.QUIZ_ID) {
                type = NavType.StringType
            })
        ) { navStack ->
            val quizId =
                navStack.arguments?.getString(NavParams.QUIZ_ID) ?: ""
            val quiz =
                navHost
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<QuizParcelable>(NavParams.QUIZ_TAG)
            CreateQuestions(
                navController = navHost,
                quizId = quizId,
                parcelable = quiz
            )
        }
    }
}