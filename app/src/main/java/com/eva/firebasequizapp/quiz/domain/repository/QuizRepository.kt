package com.eva.firebasequizapp.quiz.domain.repository

import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun getAllQuizzes(): Flow<Resource<List<QuizModel?>>>
    suspend fun getCurrentUserContributedQuiz(uid: String): Flow<Resource<List<QuizModel?>>>

}