package com.eva.firebasequizapp.quiz.util

import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.eva.firebasequizapp.quiz.domain.models.QuizModel

data class FullQuizState(
    val isLoading: Boolean = true,
    val questions: List<QuestionModel?> = emptyList(),
    val isQuestionLoading:Boolean = true,
    val isQuizPresent: Boolean = true,
    val quiz: QuizModel? = null
)
