package com.eva.firebasequizapp.contribute_quiz.data.dto

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class CreateQuestionDto(
    val question: String = "",
    val description: String? = null,
    @PropertyName("required")
    val isRequired: Boolean = false,
    val options: List<String> = emptyList(),
    val correctAnswer: String? = null,
    val quizId: DocumentReference? = null
)

