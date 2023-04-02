package com.eva.firebasequizapp.auth.util

import com.eva.firebasequizapp.auth.domain.models.UserAuthModel

data class UserFormState(
    val email: String = "",
    val emailMessage: String? = null,
    val password: String = "",
    val passwordMessage: String? = null
) {
    fun toModel(): UserAuthModel {
        return UserAuthModel(
            email = email.trim(),
            password = password.trim()
        )
    }
}

