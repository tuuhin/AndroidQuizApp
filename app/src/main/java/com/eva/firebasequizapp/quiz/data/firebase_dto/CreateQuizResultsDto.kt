package com.eva.firebasequizapp.quiz.data.firebase_dto

import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class CreateQuizResultsDto(
    @get:PropertyName(FireStoreCollections.QUID_ID_FIELD)
    val quizId: DocumentReference? = null,
    @get:PropertyName(FireStoreCollections.USER_ID_FIELD)
    val userId: String? = null,
    @get:PropertyName(FireStoreCollections.TOTAL_QUESTION_FIELD)
    val totalQuestions: Int = 0,
    @get:PropertyName(FireStoreCollections.CORRECT_ANS_FIELD)
    val correctAnswer: Int = 0,
) {
    fun updateMap(): Map<String, Int> =
        hashMapOf(
            FireStoreCollections.TOTAL_QUESTION_FIELD to totalQuestions,
            FireStoreCollections.CORRECT_ANS_FIELD to correctAnswer
        )

}

