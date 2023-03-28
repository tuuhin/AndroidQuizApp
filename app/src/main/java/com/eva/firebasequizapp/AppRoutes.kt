package com.eva.firebasequizapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eva.firebasequizapp.contribute_quiz.presentation.ContributedQuestions
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestions
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuiz
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable
import com.eva.firebasequizapp.quiz.presentation.CurrentQuizRoute
import com.eva.firebasequizapp.quiz.presentation.QuizRoute

@Composable
fun AppRoutes() {
    val navHost = rememberNavController()
    NavHost(
        navController = navHost,
        startDestination = NavRoutes.NavHomeRoute.route
    ) {
        composable(NavRoutes.NavHomeRoute.route) {
            QuizRoute(navController = navHost)
        }
        composable(NavRoutes.NavCreateQuizRoute.route) {
            CreateQuiz(navController = navHost)
        }
        composable(
            NavRoutes.NavQuizRoute.route + NavParams.QUIZ_PARAM_ID,
            arguments = listOf(navArgument(NavParams.QUIZ_ID) {
                type = NavType.StringType
            })
        ) { _ ->
            val parcelable = navHost
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<QuizParcelable>(NavParams.QUIZ_TAG)
            CurrentQuizRoute(navController = navHost, parcelable = parcelable)
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
            ContributedQuestions(
                navController = navHost,
                quizId = quizId,
                parcelable = quiz
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