package com.eva.firebasequizapp.contribute_quiz.domain.use_cases

import com.eva.firebasequizapp.contribute_quiz.util.CreateQuizState
import com.eva.firebasequizapp.core.models.Validator

class CreateQuizValidator {

    fun validateSubject(quiz: CreateQuizState): Validator {
        return if (quiz.subject.isEmpty()) Validator(
            isValid = false,
            message = "Cannot add a empty subject "
        ) else Validator(isValid = true)
    }

    fun validateDesc(quiz: CreateQuizState): Validator {
        return if (quiz.desc.isEmpty()) Validator(
            isValid = false,
            message = "Cannot add a empty description subject "
        ) else Validator(isValid = true)
    }
}