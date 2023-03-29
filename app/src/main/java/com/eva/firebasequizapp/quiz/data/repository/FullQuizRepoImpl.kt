package com.eva.firebasequizapp.quiz.data.repository

import android.util.Log
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuestionsDto
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuizDto
import com.eva.firebasequizapp.quiz.data.mapper.toDto
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.models.CreateQuizResultModel
import com.eva.firebasequizapp.quiz.domain.repository.FullQuizRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FullQuizRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val user: FirebaseUser?,
) : FullQuizRepository {
    override suspend fun getAllQuestions(quiz: String): Flow<Resource<List<QuestionModel?>>> {
        val docPath = "/" + FireStoreCollections.QUIZ_COLLECTION + "/" + quiz
        return flow {
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
                    snapshot.toObject<QuestionsDto>()?.toModel()
                }
                Log.d("QU", questions.toString())
                emit(Resource.Success(questions))
            } catch (e: FirebaseFirestoreException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Firebase Exception occurred"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Unknown exception occurred"))
            }
        }
    }

    override suspend fun getCurrentQuiz(uid: String): Flow<Resource<QuizModel?>> {
        return flow {
            val docPath = FireStoreCollections.QUIZ_COLLECTION + "/$uid"
            try {
                val quizDto = fireStore.collection(FireStoreCollections.QUIZ_COLLECTION)
                    .document(docPath)
                    .get()
                    .await()
                val quizModel = quizDto.toObject<QuizDto>()?.toModel()
                emit(Resource.Success(quizModel))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message ?: "Firebase exception occurred"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Exception occurred"))
            }
        }
    }

    override suspend fun setResult(result: CreateQuizResultModel): Flow<Resource<Boolean>> {
        val dto = result.toDto(fireStore, user!!)
        val query =
            fireStore.collection(FireStoreCollections.RESULT_COLLECTIONS)
                .whereEqualTo(FireStoreCollections.QUID_ID_FIELD, dto.quizId)
                .whereEqualTo(FireStoreCollections.USER_ID_FIELD, user.uid)
        return flow {
            try {
                val queriedData = query.get().await()
                if (queriedData.documents.isNotEmpty()) {
                    val doc = queriedData.documents.first()
                    if (doc.exists()) {
                        fireStore.collection(FireStoreCollections.RESULT_COLLECTIONS)
                            .document(doc.id)
                            .update(dto.updateMap()).await()
                    }
                } else {
                    fireStore.collection(FireStoreCollections.RESULT_COLLECTIONS)
                        .add(dto)
                        .await()
                }
                emit(Resource.Success(true))
            } catch (e: FirebaseFirestoreException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "FireStore exception"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Unknown error "))
            }
        }
    }
}