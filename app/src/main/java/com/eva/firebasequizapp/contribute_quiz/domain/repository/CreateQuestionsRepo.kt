package com.eva.firebasequizapp.contribute_quiz.domain.repository

import com.eva.firebasequizapp.contribute_quiz.domain.models.CreateQuestionsModel
import com.eva.firebasequizapp.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface CreateQuestionsRepo {
    suspend fun createQuestionsToQuiz(questions: List<CreateQuestionsModel>)
            : Flow<Resource<Unit>>

}