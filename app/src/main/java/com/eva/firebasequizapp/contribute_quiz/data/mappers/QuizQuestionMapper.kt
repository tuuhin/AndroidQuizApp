package com.eva.firebasequizapp.contribute_quiz.data.mappers

import com.eva.firebasequizapp.contribute_quiz.data.dto.CreateQuestionDto
import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuestionsModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.google.firebase.firestore.FirebaseFirestore


fun CreateQuestionState.toModel(): CreateQuestionsModel = CreateQuestionsModel(
    question = question,
    description = desc,
    isRequired = required,
    options = options.map { it.option },
    correctAnswer = ansKey?.option
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