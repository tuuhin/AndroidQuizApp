package com.eva.firebasequizapp.auth.data

import com.eva.firebasequizapp.core.util.Resource
import com.google.firebase.auth.FirebaseUser

interface UserAuthRepository {

    suspend fun signInUsingEmailAndPassword(email: String, password: String):Resource<FirebaseUser>

    suspend fun signUpUsingEmailAndPassword(email: String,password: String):Resource<FirebaseUser>

    suspend fun signAnonymously():Resource<FirebaseUser>

     fun logout()
}