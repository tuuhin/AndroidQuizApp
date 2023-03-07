package com.eva.firebasequizapp.auth.domain.useCases

class PasswordValidatorUseCase {

    fun execute(password: String): Validator {
        return if (password.isEmpty()) {
            Validator(isValid = false, message = "Password is blank")
        } else if (!(password.any { it.isDigit() } && password.any { it.isLetter() }))
            Validator(isValid = false, message = "Password should contain a letter and a character")
        else if (password.length < 4) {
            Validator(
                isValid = false,
                message = "Password is too small"
            )
        } else {
            Validator(isValid = true)
        }
    }
}