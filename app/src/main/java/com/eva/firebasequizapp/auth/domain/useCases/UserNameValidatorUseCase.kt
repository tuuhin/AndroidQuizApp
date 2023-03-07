package com.eva.firebasequizapp.auth.domain.useCases

class UserNameValidatorUseCase {

    fun execute(username: String): Validator {
        return if (username.isEmpty()) {
            Validator(isValid = false, message = "Cannot have a empty string here")
        } else if (username.length < 3) {
            Validator(isValid = false, message = "The username is too short")
        } else {
            Validator(isValid = true)
        }
    }
}