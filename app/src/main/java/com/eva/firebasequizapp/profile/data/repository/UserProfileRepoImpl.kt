package com.eva.firebasequizapp.profile.data.repository

import android.net.Uri
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.profile.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepoImpl @Inject constructor(
    private val user: FirebaseUser?
) : UserProfileRepository {
    override suspend fun updateUserName(name: String): Resource<Unit> {
        return try {
            user!!.updateProfile(userProfileChangeRequest { displayName = name }).await()
            Resource.Success(Unit)
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Firebase exception")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Some error occurred")
        }
    }

    override suspend fun updateImage(uri: Uri): Resource<Unit> {
        return try {
            user!!.updateProfile(userProfileChangeRequest { photoUri = uri }).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Some error occurred")
        }
    }
}