package com.eva.firebasequizapp.quiz.domain.repository

import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.models.CreateQuizResultModel
import kotlinx.coroutines.flow.Flow

interface FullQuizRepository {

    suspend fun getAllQuestions(quiz: String): Flow<Resource<List<QuestionModel?>>>

    suspend fun getCurrentQuiz(uid: String): Resource<QuizModel?>

    suspend fun setResult(result: CreateQuizResultModel): Flow<Resource<Boolean>>
}