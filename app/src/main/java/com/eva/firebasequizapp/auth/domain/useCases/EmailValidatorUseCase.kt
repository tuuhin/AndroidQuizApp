package com.eva.firebasequizapp.auth.domain.useCases

import com.eva.firebasequizapp.core.models.Validator

class EmailValidatorUseCase {

    private val emailPattern =
        Regex("/[a-zA-Z\\d.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z\\d-]+(?:\\.[a-zA-Z\\d-]+)*/")

    fun execute(email: String): Validator {
        return if (email.isEmpty()) {
            Validator(isValid = false, message = "Cannot have an empty email")
        } else if (emailPattern.matches(email)) {
            Validator(isValid = false, message = "Email structure don't matches ")
        }else{
            Validator(isValid = true)
        }
    }
}