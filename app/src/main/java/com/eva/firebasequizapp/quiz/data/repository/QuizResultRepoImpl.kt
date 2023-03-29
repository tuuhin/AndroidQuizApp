package com.eva.firebasequizapp.quiz.data.repository

import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuizDto
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuizResultsDto
import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizResultsRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizResultRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val user: FirebaseUser?,
) : QuizResultsRepository {
    override suspend fun getQuizResults(): Flow<Resource<List<QuizResultModel?>>> {
        val query = fireStore.collection(FireStoreCollections.RESULT_COLLECTIONS)
            .whereEqualTo(FireStoreCollections.USER_ID_FIELD, user!!.uid)

        var job: Job? = null
        return callbackFlow {
            val callback = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Firebase Error"))
                    close(error)
                }
                try {
                    job = launch {
                        val data = snapshot?.documents?.map { doc ->
                            val quidId =
                                doc.data?.get(FireStoreCollections.QUID_ID_FIELD) as DocumentReference
                            val quizData = quidId.get().await().toObject<QuizDto>()
                            doc.toObject<QuizResultsDto>()?.copy(quizDto = quizData)?.toModel()
                        } ?: emptyList()
                        trySend(Resource.Success(data))
                    }
                } catch (e: Exception) {
                    trySend(Resource.Error(e.message ?: "Unknown error"))
                }
            }
            awaitClose {
                job?.cancel()
                callback.remove()
            }
        }
    }
}