package com.eva.firebasequizapp.contribute_quiz.domain.repository

import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import kotlinx.coroutines.flow.Flow

interface QuestionsRepository {

    suspend fun getQuestions(quiz: String): Flow<Resource<List<QuestionModel?>>>

    suspend fun deleteQuestion(questionModel: QuestionModel): Flow<Resource<Boolean>>

    suspend fun deleteQuiz(quizId: String): Flow<Resource<Boolean>>
}