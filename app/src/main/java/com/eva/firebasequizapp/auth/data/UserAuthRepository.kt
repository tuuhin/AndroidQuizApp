package com.eva.firebasequizapp.auth.data

import com.eva.firebasequizapp.core.util.Resource
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface UserAuthRepository {

    suspend fun signInUsingEmailAndPassword(email: String, password: String): Resource<FirebaseUser>

    suspend fun signUpUsingEmailAndPassword(
        email: String,
        password: String,
        username: String
    ): Resource<FirebaseUser>

    suspend fun signAnonymously(): Resource<FirebaseUser>

    suspend fun signInWithGoogle(authCredential: AuthCredential): Resource<FirebaseUser>
    fun logout()
}