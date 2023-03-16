package com.eva.firebasequizapp.contribute_quiz.data.mappers

import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuizModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizState
import okhttp3.internal.toHexString

fun CreateQuizState.toModel(): CreateQuizModel {
    return CreateQuizModel(
        subject = subject,
        desc = desc,
        color = color?.let { "#${it.toHexString()}" },
        image = image?.toString(),
        createdBy = createdBy,
        creatorUID = creatorUID
    )
}