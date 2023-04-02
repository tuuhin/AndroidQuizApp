package com.eva.firebasequizapp.contribute_quiz.util

data class DeleteWholeQuizState(
    val showDialog: Boolean = false,
    val quizId: String? = null,
    val isDeleting: Boolean = false
)

sealed class DeleteQuizEvents {
    data class PickQuiz(val quizId: String) : DeleteQuizEvents()
    object OnDeleteConfirmed : DeleteQuizEvents()
    object OnDeleteCanceled : DeleteQuizEvents()
}
