package com.eva.firebasequizapp.auth.domain.repository

import com.eva.firebasequizapp.auth.domain.models.UserAuthModel
import com.eva.firebasequizapp.core.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserAuthRepository {

    suspend fun signInUsingEmailAndPassword(user:UserAuthModel): Flow<Resource<FirebaseUser>>

    suspend fun signUpUsingEmailAndPassword(user: UserAuthModel): Flow<Resource<FirebaseUser>>

    suspend fun signAnonymously(): Resource<FirebaseUser>

    suspend fun signInWithGoogle(authCredential: AuthCredential): Flow<Resource<FirebaseUser>>
    fun logout()
}