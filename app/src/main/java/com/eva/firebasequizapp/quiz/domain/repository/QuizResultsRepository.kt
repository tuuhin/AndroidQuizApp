package com.eva.firebasequizapp.quiz.domain.repository

import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel
import kotlinx.coroutines.flow.Flow

interface QuizResultsRepository {

    suspend fun getQuizResults(): Flow<Resource<List<QuizResultModel?>>>
}