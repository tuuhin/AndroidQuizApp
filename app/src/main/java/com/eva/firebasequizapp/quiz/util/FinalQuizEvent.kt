package com.eva.firebasequizapp.quiz.util

import com.eva.firebasequizapp.quiz.domain.models.QuestionModel


data class FinalQuizOptionState(
    val option: String? = null,
    val isCorrect: Boolean = false
)


sealed class FinalQuizEvent {
    data class OptionPicked(val index: Int, val option: String, val question: QuestionModel) :
        FinalQuizEvent()

    data class OptionUnpicked(val index: Int, val option: String, val question: QuestionModel) :
        FinalQuizEvent()

    object SubmitQuiz : FinalQuizEvent()
}
