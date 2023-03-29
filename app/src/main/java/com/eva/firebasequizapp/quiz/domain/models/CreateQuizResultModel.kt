package com.eva.firebasequizapp.quiz.domain.models

data class CreateQuizResultModel(
    val quizId: String,
    val totalQuestions: Int,
    val correct: Int
)
