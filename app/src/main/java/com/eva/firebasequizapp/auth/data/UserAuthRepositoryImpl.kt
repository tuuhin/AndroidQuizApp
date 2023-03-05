package com.eva.firebasequizapp.auth.data

import com.eva.firebasequizapp.core.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UserAuthRepository {
    override suspend fun signInUsingEmailAndPassword(
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(value = user.user!!)
        } catch (e: FirebaseAuthException) {
            Resource.Error(e.message ?: "FireBase Auth exception")
        } catch (e: FirebaseAuthInvalidUserException) {
            Resource.Error(e.message ?: "Invalid User Exception")
        } catch (e: FirebaseAuthUserCollisionException) {
            Resource.Error(e.message ?: "Auth collision Exception")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Exception occured")
        }
    }

    override suspend fun signUpUsingEmailAndPassword(
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(value = user.user!!)
        } catch (e: FirebaseAuthException) {
            Resource.Error(e.message ?: "FireBase Auth exception")
        } catch (e: FirebaseAuthInvalidUserException) {
            Resource.Error(e.message ?: "Invalid User Exception")
        } catch (e: FirebaseAuthUserCollisionException) {
            Resource.Error(e.message ?: "Auth collision Exception")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Exception occured")
        }

    }

    override suspend fun signAnonymously(): Resource<FirebaseUser> {
        return try {
            val user = auth.signInAnonymously().await()
            Resource.Success(value = user.user!!)
        } catch (e: FirebaseAuthException) {
            Resource.Error(e.message ?: "FireBase Auth exception")
        } catch (e: FirebaseAuthInvalidUserException) {
            Resource.Error(e.message ?: "Invalid User Exception")
        } catch (e: FirebaseAuthUserCollisionException) {
            Resource.Error(e.message ?: "Auth collision Exception")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Exception occured")
        }
    }

    override fun logout() = auth.signOut()
}