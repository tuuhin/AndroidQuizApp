package com.eva.firebasequizapp.auth.data.repository

import com.eva.firebasequizapp.auth.domain.models.UserAuthModel
import com.eva.firebasequizapp.auth.domain.repository.UserAuthRepository
import com.eva.firebasequizapp.core.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : UserAuthRepository {
    override suspend fun signInUsingEmailAndPassword(user: UserAuthModel): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            try {
                val newUser = auth.signInWithEmailAndPassword(user.email, user.password).await()
                emit(Resource.Success(value = newUser.user!!))
            } catch (e: FirebaseAuthException) {
                emit(Resource.Error(e.message ?: "FireBase Auth exception"))
            } catch (e: FirebaseAuthInvalidUserException) {
                emit(Resource.Error(e.message ?: "Invalid User Exception"))
            } catch (e: FirebaseAuthUserCollisionException) {
                emit(Resource.Error(e.message ?: "Auth collision Exception"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Exception occurred"))
            }
        }
    }

    override suspend fun signUpUsingEmailAndPassword(user:UserAuthModel)
            : Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            try {
                val newUser = auth.createUserWithEmailAndPassword(user.email, user.password).await()
                emit(Resource.Success(value = newUser.user!!))
            } catch (e: FirebaseAuthException) {
                emit(Resource.Error(e.message ?: "FireBase Auth exception"))
            } catch (e: FirebaseAuthInvalidUserException) {
                emit(Resource.Error(e.message ?: "Invalid User Exception"))
            } catch (e: FirebaseAuthUserCollisionException) {
                emit(Resource.Error(e.message ?: "Auth collision Exception"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Exception occurred"))
            }
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
            Resource.Error(e.message ?: "Exception occurred")
        }
    }

    override suspend fun signInWithGoogle(authCredential: AuthCredential)
            : Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            try {
                val user = auth.signInWithCredential(authCredential).await()
                emit(Resource.Success(value = user.user!!))
            } catch (e: FirebaseAuthException) {
                emit(Resource.Error(e.message ?: "FireBase Auth exception"))
            } catch (e: FirebaseAuthInvalidUserException) {
                emit(Resource.Error(e.message ?: "Invalid User Exception"))
            } catch (e: FirebaseAuthUserCollisionException) {
                emit(Resource.Error(e.message ?: "Auth collision Exception"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Exception occurred"))
            }
        }
    }

    override fun logout(): Unit = auth.signOut()
}