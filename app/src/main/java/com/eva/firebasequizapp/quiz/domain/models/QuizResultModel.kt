package com.eva.firebasequizapp.quiz.domain.models

data class QuizResultModel(
    val uid: String? = null,
    val totalQuestions: Int,
    val correct: Int,
    val quiz: QuizModel? = null
)
