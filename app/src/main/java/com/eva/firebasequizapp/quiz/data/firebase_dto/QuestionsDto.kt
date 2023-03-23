package com.eva.firebasequizapp.quiz.data.firebase_dto

import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class QuestionsDto(
    @DocumentId val id: String = "",
    val question: String = "",
    val description: String? = null,
    @PropertyName("required")
    val isRequired: Boolean = false,
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
) {
    fun toModel(): QuestionModel {
        return QuestionModel(
            uid = id,
            question = question,
            isRequired = isRequired,
            description = description,
            options = options,
            correctAns = correctAnswer
        )
    }
}
