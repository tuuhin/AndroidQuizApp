package com.eva.firebasequizapp.contribute_quiz.domain.use_cases

import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.presentation.QuestionOptionsState
import com.eva.firebasequizapp.core.util.Validator

class CreateQuestionValidator {

    fun validateQuestion(state: CreateQuestionState): Validator {
        return if (state.question.isEmpty()) Validator(
            isValid = false,
            message = "Cannot add a blank question"
        ) else Validator(isValid = true)
    }


    fun validateOptions(state: QuestionOptionsState): Validator {
        return if (state.option.isEmpty()) Validator(
            isValid = false,
            message = "Cannot add a blank option"
        ) else Validator(isValid = true)
    }
}