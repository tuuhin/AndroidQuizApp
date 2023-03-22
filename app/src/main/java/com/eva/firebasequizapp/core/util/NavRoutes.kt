package com.eva.firebasequizapp.core.util

sealed class NavRoutes(val route: String) {
    object NavHomeRoute : NavRoutes("/")
    object NavCreateQuizRoute : NavRoutes("/create_quiz")
    object NavQuizRoute : NavRoutes("/quiz")
    object NavAddQuestionsRoute : NavRoutes("/add-questions")
}
