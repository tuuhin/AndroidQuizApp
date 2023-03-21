package com.eva.firebasequizapp.contribute_quiz.domain.models



data class CreateQuizModel(
    val subject: String,
    val desc: String,
    val color: String,
    val creatorUID: String,
    val image: String? = null,
)
