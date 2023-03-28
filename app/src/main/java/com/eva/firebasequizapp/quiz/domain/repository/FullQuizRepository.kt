package com.eva.firebasequizapp.quiz.domain.repository

import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import kotlinx.coroutines.flow.Flow

interface FullQuizRepository {

    suspend fun getAllQuestions(quiz: String): Flow<Resource<List<QuestionModel?>>>

    suspend fun getCurrentQuiz(uid: String): Flow<Resource<QuizModel?>>
}