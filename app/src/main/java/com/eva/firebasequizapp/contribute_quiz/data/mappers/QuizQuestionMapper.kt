package com.eva.firebasequizapp.contribute_quiz.data.mappers

import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuestionsModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState


fun CreateQuestionState.toModel(): CreateQuestionsModel = CreateQuestionsModel(
    question = question,
    description = desc,
    isRequired = required,
    options = options.map { it.option },
    correctAnswer = ansKey?.option
)

