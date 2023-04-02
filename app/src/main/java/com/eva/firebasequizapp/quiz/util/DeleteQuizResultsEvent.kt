package com.eva.firebasequizapp.quiz.util

import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel


data class DeleteQuizResultsState(
    val isDialogOpen: Boolean = false,
    val result: QuizResultModel? = null
)

sealed class DeleteQuizResultsEvent {
    data class ResultsSelected(val result: QuizResultModel) : DeleteQuizResultsEvent()
    object DeleteConfirmed : DeleteQuizResultsEvent()
    object DeleteCanceled : DeleteQuizResultsEvent()
}
