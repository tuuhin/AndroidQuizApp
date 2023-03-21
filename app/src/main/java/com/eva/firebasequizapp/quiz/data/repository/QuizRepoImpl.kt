package com.eva.firebasequizapp.quiz.data.repository

import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuizDto
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class QuizRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : QuizRepository {
    override suspend fun getAllQuizzes(): Flow<Resource<List<QuizModel?>>> {

        val colRef = fireStore
            .collection(FireStoreCollections.QUIZ_COLLECTION)
            .orderBy(FireStoreCollections.TIMESTAMP_FIELD, Query.Direction.DESCENDING)
        return callbackFlow {
            val callback = colRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    trySend(Resource.Error(error.message ?: "Firestore exception"))
                    close(error)
                }
                try {
                    val data = snapshot?.documents?.map { member ->
                        member.toObject<QuizDto>()?.toModel()
                    } ?: emptyList()
                    trySend(Resource.Success(data))
                } catch (e: Exception) {
                    e.printStackTrace()
                    trySend(Resource.Error(e.message ?: "SOme exception occurred"))
                }
            }
            awaitClose { callback.remove() }
        }
    }

    override suspend fun getCurrentUserContributedQuiz(uid: String): Flow<Resource<List<QuizModel?>>> {
        val colRef = fireStore
            .collection(FireStoreCollections.QUIZ_COLLECTION)
            .whereEqualTo(FireStoreCollections.CREATOR_UID, uid)

        return callbackFlow {
            val callback = colRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    close(error)
                }
                try {
                    val data = snapshot?.documents?.map { member ->
                        member.toObject<QuizDto>()?.toModel()
                    } ?: emptyList()
                    trySend(Resource.Success(data))
                } catch (e: Exception) {
                    e.printStackTrace()
                    trySend(Resource.Error(e.message ?: "Error Occurred"))
                }
            }
            awaitClose { callback.remove() }
        }
    }

}