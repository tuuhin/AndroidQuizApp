package com.eva.firebasequizapp.profile.data.repository

import android.net.Uri
import com.eva.firebasequizapp.core.firebase_paths.StoragePaths
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.profile.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepoImpl @Inject constructor(
    private val user: FirebaseUser?,
    private val storage: FirebaseStorage,
) : UserProfileRepository {

    private suspend fun  setImage(uri: Uri):Uri {
        val pathString = "${user!!.uid}/${StoragePaths.IMAGE_PATHS}/${uri.lastPathSegment}"
        val fileRef = storage.reference.child(pathString)
        val task = fileRef.putFile(uri).await()
        return task.storage.downloadUrl.await()
    }

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
            val newUri = setImage(uri)
            user!!.updateProfile(userProfileChangeRequest { photoUri = newUri }).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Some error occurred")
        }
    }
}