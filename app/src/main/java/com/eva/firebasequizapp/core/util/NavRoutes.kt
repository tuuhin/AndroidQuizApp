package com.eva.firebasequizapp.core.util

sealed class NavRoutes(val route: String) {
    object NavHomeRoute : NavRoutes("/")
    object NavCreateQuizRoute : NavRoutes("/create_quiz")
    object NavQuizRoute : NavRoutes("/quiz")
    object NavAddQuestionsRoute : NavRoutes("/add-questions")
}

object NavParams {
    const val QUIZ_ID: String = "quizId"
    const val QUIZ_PARAM_ID: String = "/{$QUIZ_ID}"
}