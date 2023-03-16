package com.eva.firebasequizapp.contribute_quiz.domain.models

data class CreateQuestionsModel(
    val question: String = "",
    val description: String? = null,
    val isRequired: Boolean = false,
    val options: List<String> = emptyList(),
    val correctAnswer: String? = null,
    val image: String? = null
)
