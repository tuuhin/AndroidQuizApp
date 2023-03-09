package com.eva.firebasequizapp.quiz.data.repository

import android.util.Log
import com.eva.firebasequizapp.quiz.data.firebase_dto.QuizDto
import com.eva.firebasequizapp.quiz.domain.FireStoreCollections
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : QuizRepository {
    override suspend fun getQuiz(): Flow<List<QuizModel?>> {

        val colRef = fireStore.collection(FireStoreCollections.QUIZ_COLLECTION)
        var scope: Job? = null
        return callbackFlow {
            val callback = colRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    close(error)
                }
                try {
                    val data = snapshot?.documents?.map { member ->
                        val obj = member.toObject<QuizDto>()
                        Log.d("DATA",obj.toString())
                        obj?.toModel()
                    } ?: emptyList()
                    Log.d("DATA",data.toString())
                    scope = launch { send(data) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            awaitClose {
                scope?.cancel()
                callback.remove()
            }
        }
    }

}