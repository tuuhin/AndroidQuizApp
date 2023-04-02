package com.eva.firebasequizapp.quiz.domain.models

data class QuizResultModel(
    val uid: String ,
    val totalQuestions: Int,
    val correct: Int,
    val quiz: QuizModel? = null
)
