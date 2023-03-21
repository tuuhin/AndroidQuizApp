package com.eva.firebasequizapp.contribute_quiz.data.repository

import android.net.Uri
import com.eva.firebasequizapp.contribute_quiz.data.mappers.toDto
import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuizModel
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuizRepository
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.firebase_paths.StoragePaths
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuizDto
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CreateQuizRepoImpl @Inject constructor(
    private val user: FirebaseUser?,
    private val storage: FirebaseStorage,
    private val fireStore: FirebaseFirestore,
) : CreateQuizRepository {
    override suspend fun createQuiz(quiz: CreateQuizModel): Flow<Resource<QuizModel?>> {
        return flow {
            try {
                val data =
                    fireStore
                        .collection(FireStoreCollections.QUIZ_COLLECTION)
                        .add(quiz.toDto())
                        .await()
                val quizDto = data
                    .get()
                    .await()
                    .toObject<QuizDto>()
                emit(Resource.Success(quizDto?.toModel()))
            } catch (e: FirebaseFirestoreException) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message ?: "FireStore message error"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun uploadQuizImage(path: String): String {
        val uri = Uri.parse(path)
        val pathString = "${user!!.uid}/${StoragePaths.QUIZ_PATH}/${uri.lastPathSegment}"
        val fileRef = storage.reference.child(pathString)
        val task = fileRef.putFile(uri).await()
        val outUri = task.storage.downloadUrl.await()
        return outUri.toString()
    }
}