package com.eva.firebasequizapp.contribute_quiz.data.repository

import com.eva.firebasequizapp.contribute_quiz.domain.repository.QuestionsRepository
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuestionsDto
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : QuestionsRepository {
    override suspend fun getQuestions(quiz: String): Flow<Resource<List<QuestionModel?>>> {
        val docPath = "/" + FireStoreCollections.QUIZ_COLLECTION + "/" + quiz
        return callbackFlow {
            trySend(Resource.Loading())
            val callback = fireStore
                .collection(FireStoreCollections.QUESTION_COLLECTION)
                .whereEqualTo(
                    FireStoreCollections.QUID_ID_FIELD,
                    fireStore.document(docPath)
                )
                .addSnapshotListener { snap, error ->
                    if (error != null) {
                        trySend(Resource.Error(error.message ?: "Firebase error"))
                        close()
                    }
                    try {
                        val questions =
                            snap?.documents?.map { snapshot -> snapshot.toObject<QuestionsDto>() }
                                ?.map { it?.toModel() } ?: emptyList()
                        trySend(Resource.Success(questions))
                    } catch (e: Exception) {
                        trySend(Resource.Error(message = e.message ?: "Unknown error"))
                    }
                }
            awaitClose { callback.remove() }
        }
    }

    override suspend fun deleteQuestion(questionModel: QuestionModel): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                fireStore
                    .collection(FireStoreCollections.QUESTION_COLLECTION)
                    .document(questionModel.uid)
                    .delete()
                    .await()
                emit(Resource.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message ?: "Firebase exception occurred"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown exception occurred "))
            }
        }
    }

    override suspend fun deleteQuiz(quizId: String): Flow<Resource<Boolean>> {
        return flow {
            try {
                val quizDocPath = "/${FireStoreCollections.QUIZ_COLLECTION}/${quizId}"
                val documentRef = fireStore.document(quizDocPath)
                val query = fireStore
                    .collection(FireStoreCollections.QUESTION_COLLECTION)
                    .whereEqualTo(FireStoreCollections.QUID_ID_FIELD, documentRef)
                val data = query.get().await()

                val deleteQuery = data.documents
                    .map { snapshot ->
                        fireStore
                            .document(snapshot.reference.path)
                            .delete()
                            .asDeferred()
                    }
                deleteQuery.awaitAll()
                val resultQuery = fireStore
                    .collection(FireStoreCollections.RESULT_COLLECTIONS)
                    .whereEqualTo(FireStoreCollections.QUID_ID_FIELD, documentRef)
                val resultData = resultQuery.get().await()

                val deleteResult = resultData.documents
                    .map { snapshot ->
                        fireStore
                            .document(snapshot.reference.path)
                            .delete()
                            .asDeferred()
                    }
                deleteResult.awaitAll()
                fireStore
                    .collection(FireStoreCollections.QUIZ_COLLECTION)
                    .document(quizId)
                    .delete()
                    .await()
                emit(Resource.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message ?: "FIREBASE EXCEPTION"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "UNKNOWN EXCEPTION"))
            }
        }
    }
}