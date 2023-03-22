package com.eva.firebasequizapp.auth.util

sealed class UserFormEvents {
    data class EmailChanged(val email: String) : UserFormEvents()
    data class PasswordChanged(val password: String) : UserFormEvents()
    object FormSubmit : UserFormEvents()
}