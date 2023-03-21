package com.eva.firebasequizapp.contribute_quiz.data.repository

import android.util.Log
import com.eva.firebasequizapp.contribute_quiz.data.mappers.toDto
import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuestionsModel
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuestionsRepo
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CreateQuestionRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : CreateQuestionsRepo {
    override suspend fun createQuestionsToQuiz(
        questions: List<CreateQuestionsModel>
    ): Flow<Resource<List<QuestionModel?>>> {
        val collPath = fireStore
            .collection(FireStoreCollections.QUESTION_COLLECTION)
        return flow {
            try {
                questions
                    .map { model -> model.toDto(fireStore) }
                    .map { dto -> collPath.add(dto).asDeferred()
                    }
                    .awaitAll()
                emit(Resource.Success(emptyList()))
            } catch (e: FirebaseFirestoreException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "FIREBASE ERROR OCCURRED"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "EXCEPTION OCCURRED"))
            }

        }
    }

    override suspend fun getTitleFromUid(uid: String): Resource<String?> {
        val quizCollection = fireStore.collection(FireStoreCollections.QUESTION_COLLECTION)
        return try {
            val document =
                quizCollection.document(FireStoreCollections.QUESTION_COLLECTION + "/$uid").get()
                    .await()
            val result = document.getString("subject")
            Log.d("TAG", result.toString())
            Resource.Success(result)
        } catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Firestore exception")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Exception")
        }
    }
}