package com.eva.firebasequizapp.contribute_quiz.data.dto

import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuestionsModel
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

data class CreateQuestionDto(
    val question: String = "",
    val description: String? = null,
    val isRequired: Boolean = false,
    val options: List<String> = emptyList(),
    val correctAnswer: String? = null,
    val quizId: DocumentReference? = null
)

fun CreateQuestionsModel.toDto(
    firestore: FirebaseFirestore
): CreateQuestionDto {
    return CreateQuestionDto(
        question = question,
        description = description,
        isRequired = isRequired,
        options = options,
        correctAnswer = correctAnswer,
        quizId = quizId?.let { id ->
            firestore.document(FireStoreCollections.QUIZ_COLLECTION + "/$id")
        }
    )
}