package com.eva.firebasequizapp.quiz.util

data class FinalQuizRouteState(
    val showDialog: Boolean = false,
    val isBackNotAllowed: Boolean = true
)

sealed class FinalQuizDialogEvents {
    object ContinueQuiz : FinalQuizDialogEvents()
    object SubmitQuiz : FinalQuizDialogEvents()
}