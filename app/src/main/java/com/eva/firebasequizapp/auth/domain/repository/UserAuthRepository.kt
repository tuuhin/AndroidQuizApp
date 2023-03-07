package com.eva.firebasequizapp.auth.domain.repository

import com.eva.firebasequizapp.core.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserAuthRepository {

    suspend fun signInUsingEmailAndPassword(email: String, password: String): Flow<Resource<FirebaseUser>>

    suspend fun signUpUsingEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<FirebaseUser>>

    suspend fun signAnonymously(): Resource<FirebaseUser>

    suspend fun signInWithGoogle(authCredential: AuthCredential): Flow<Resource<FirebaseUser>>
    fun logout()
}