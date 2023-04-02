package com.eva.firebasequizapp.quiz.domain.use_case

import com.eva.firebasequizapp.core.models.Validator

class DocumentIdValidator {
    // NOT SURE IF THIS IS THE CORRECT PATTERN
    private val regex = Regex("^(?!\\.\\.?\$)(?!.*__.*__)([^/]{1,1500})\$")

    fun execute(document: String): Validator {
        return if (document.isEmpty()) {
            Validator(isValid = false, message = "Cannot have an empty string")
        } else if (!regex.matches(document)) {
            Validator(isValid = false, message = "Id's aren't structured like this,")
        } else Validator(isValid = true)
    }
}