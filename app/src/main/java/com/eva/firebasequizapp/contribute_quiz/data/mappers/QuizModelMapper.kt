package com.eva.firebasequizapp.contribute_quiz.data.mappers

import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuizModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizState

fun CreateQuizState.toModel(): CreateQuizModel = CreateQuizModel(
    subject = subject,
    desc = desc,
    color = color?.let {
        "#" + it.toString(16).removeSuffix("00000000")
    },
    image = image?.toString(),
    createdBy = createdBy,
    creatorUID = creatorUID
)
