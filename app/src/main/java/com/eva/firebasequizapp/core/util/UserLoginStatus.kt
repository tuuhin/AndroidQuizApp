package com.eva.firebasequizapp.core.util

import com.google.firebase.auth.FirebaseUser

sealed class UserLoginStatus {
    object UserLoggerOut : UserLoginStatus()
    data class UserLoggedIn(val user: FirebaseUser) : UserLoginStatus()
}
