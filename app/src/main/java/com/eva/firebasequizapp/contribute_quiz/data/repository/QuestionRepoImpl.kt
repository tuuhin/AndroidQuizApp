package com.eva.firebasequizapp.contribute_quiz.data.repository

import com.eva.firebasequizapp.contribute_quiz.domain.repository.QuestionsRepository
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuestionsDto
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : QuestionsRepository {
    override suspend fun getQuestions(quiz: String): Flow<Resource<List<QuestionModel?>>> {
        val docPath = "/" + FireStoreCollections.QUIZ_COLLECTION + "/" + quiz
        return flow {
            emit(Resource.Loading())
            try {
                val query =
                    fireStore
                        .collection(FireStoreCollections.QUESTION_COLLECTION)
                        .whereEqualTo(
                            FireStoreCollections.QUID_ID_FIELD,
                            fireStore.document(docPath)
                        )
                        .get()
                        .await()
                val questions = query.documents.map { snapshot ->
                    snapshot.toObject<QuestionsDto>()
                }.map { it?.toModel() }
                emit(Resource.Success(questions))
            } catch (e: FirebaseFirestoreException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Firebase exception occurred"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Unknown exception occurred "))
            }

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
}