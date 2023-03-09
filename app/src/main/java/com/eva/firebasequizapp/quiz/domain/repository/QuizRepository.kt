package com.eva.firebasequizapp.quiz.domain.repository

import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun getQuiz(): Flow<List<QuizModel?>>
}