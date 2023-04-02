package com.eva.firebasequizapp.quiz.util

data class SearchQuizState(
    val showDialog: Boolean = false,
    val quizId: String = "",
    val quizIdError: String? = null,
    val isConfirmed: Boolean = false
)


sealed class SearchQuizEvents {
    data class OnQuizIdChanged(val id: String) : SearchQuizEvents()
    object Search : SearchQuizEvents()
    object OnSearchConfirmed : SearchQuizEvents()
    object OnSearchCancelled : SearchQuizEvents()
}
