package com.eva.firebasequizapp.quiz.data.mapper

import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.quiz.data.firebase_dto.CreateQuizResultsDto
import com.eva.firebasequizapp.quiz.domain.models.CreateQuizResultModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

fun CreateQuizResultModel.toDto(fireStore: FirebaseFirestore, user: FirebaseUser): CreateQuizResultsDto {
    return CreateQuizResultsDto(
        totalQuestions = totalQuestions,
        correctAnswer = correct,
        quizId = fireStore.document(FireStoreCollections.QUIZ_COLLECTION + "/$quizId"),
        userId = user.uid
    )
}