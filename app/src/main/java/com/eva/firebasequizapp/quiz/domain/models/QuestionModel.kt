package com.eva.firebasequizapp.quiz.domain.models

data class QuestionModel(
    val uid: String,
    val question: String,
    val description: String? = null,
    val isRequired: Boolean,
    val options: List<String>,
    val correctAns: String,
)
