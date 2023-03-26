package com.eva.firebasequizapp.contribute_quiz.data.repository

import com.eva.firebasequizapp.contribute_quiz.data.mappers.toDto
import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuestionsModel
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuestionsRepo
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

class CreateQuestionRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : CreateQuestionsRepo {
    override suspend fun createQuestionsToQuiz(
        questions: List<CreateQuestionsModel>
    ): Flow<Resource<Unit>> {
        val collPath = fireStore
            .collection(FireStoreCollections.QUESTION_COLLECTION)
        return flow {
            emit(Resource.Loading())
            try {
                questions
                    .map { model -> model.toDto(fireStore) }
                    .map { dto -> collPath.add(dto).asDeferred() }
                    .awaitAll()
                emit(Resource.Success(Unit))
            } catch (e: FirebaseFirestoreException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "FIREBASE ERROR OCCURRED"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "EXCEPTION OCCURRED"))
            }

        }
    }
}