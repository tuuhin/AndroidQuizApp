package com.eva.firebasequizapp.contribute_quiz.util

import androidx.compose.runtime.mutableStateListOf

data class CreateQuestionState(
    val question: String = "",
    val questionError: String? = null,
    val desc: String? = null,
    val required: Boolean = false,
    val state: QuestionsViewMode = QuestionsViewMode.Editable,
    val isVerified: Boolean = false,
    val isDeleteAllowed: Boolean = true,
    // The option index better to keep it this way
    val ansKey: QuestionOptionsState? = null,
    val options: MutableList<QuestionOptionsState> = mutableStateListOf(QuestionOptionsState())
)

