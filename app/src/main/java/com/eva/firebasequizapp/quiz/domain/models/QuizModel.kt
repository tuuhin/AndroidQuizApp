package com.eva.firebasequizapp.quiz.domain.models

import java.time.LocalDateTime

data class QuizModel(
    val uid: String,
    val subject: String,
    val desc: String? = null,
    val image: String? = null,
    val creatorUID: String? = null,
    val color: String? = null,
    val lastUpdate: LocalDateTime,
)