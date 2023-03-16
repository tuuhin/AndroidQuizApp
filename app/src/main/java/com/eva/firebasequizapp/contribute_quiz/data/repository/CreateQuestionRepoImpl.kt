package com.eva.firebasequizapp.contribute_quiz.data.repository

import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuestionsModel
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuestionsRepo
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

class CreateQuestionRepoImpl @Inject constructor(
    private val user: FirebaseUser?,
    private val fireStore: FirebaseFirestore,
) : CreateQuestionsRepo {
    override suspend fun createQuestionsToQuiz(questions: List<CreateQuestionsModel>):
            Flow<Resource<List<QuestionModel?>>> {
        return flow {
            try {
                val addedQuestions = questions.map {
                    fireStore.collection(FireStoreCollections.QUESTION_COLLECTION).add(it)
                        .asDeferred()
                }.awaitAll().map { ref ->
                    ref.get().asDeferred()
                }.awaitAll().map { snapshot ->
                    snapshot.toObject<QuestionModel>()
                }
                emit(Resource.Success(addedQuestions))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message ?: "FIREBASE ERROR OCCURRED"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "EXCEPTION OCCURRED"))
            }

        }
    }
}