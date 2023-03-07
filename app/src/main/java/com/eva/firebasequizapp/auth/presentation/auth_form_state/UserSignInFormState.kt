package com.eva.firebasequizapp.auth.presentation

data class UserSignInFormState(
    val email: String = "",
    val emailMessage: String? = null,
    val password: String = "",
    val passwordMessage: String? = null
)


sealed class UserSignInFormEvents {
    data class EmailChanged(val email: String) : UserSignInFormEvents()
    data class PasswordChanged(val password: String) : UserSignInFormEvents()
    object FormSubmit : UserSignInFormEvents()

}
