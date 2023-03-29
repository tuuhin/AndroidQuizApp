package com.eva.firebasequizapp.quiz.data.firebase_dto

import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class QuizResultsDto(
    @DocumentId val uid: String = "",
    @get:PropertyName(FireStoreCollections.TOTAL_QUESTION_FIELD)
    val totalQuestion: Int = 0,
    @get:PropertyName(FireStoreCollections.CORRECT_ANS_FIELD)
    val correctAns: Int = 0,
    val quizDto: QuizDto? = null
) {
    fun toModel(): QuizResultModel {
        return QuizResultModel(
            uid = uid,
            totalQuestions = totalQuestion,
            correct = correctAns,
            quiz = quizDto?.toModel()
        )
    }
}
