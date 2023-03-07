package com.eva.firebasequizapp.auth.presentation.auth_form_state

data class UserSignUpFormState(
    val email: String = "",
    val emailMessage: String? = null,
    val password: String = "",
    val passwordMessage: String? = null,
    val username: String = "",
    val usernameMessage: String? = null
)


sealed class UserSignUpFormEvent {
    data class EmailChanged(val email: String) : UserSignUpFormEvent()
    data class PasswordChanged(val password: String) : UserSignUpFormEvent()
    data class UsernameChanged(val username: String) : UserSignUpFormEvent()
    object Submit : UserSignUpFormEvent()
}
