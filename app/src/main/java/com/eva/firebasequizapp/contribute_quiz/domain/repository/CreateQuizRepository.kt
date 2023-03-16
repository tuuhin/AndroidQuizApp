package com.eva.firebasequizapp.contribute_quiz.domain.repository

import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuizModel
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import kotlinx.coroutines.flow.Flow

interface CreateQuizRepository {

    suspend fun createQuiz(quiz: CreateQuizModel): Flow<Resource<QuizModel?>>

    suspend fun uploadQuizImage(path: String): String
}