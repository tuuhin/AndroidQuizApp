package com.eva.firebasequizapp.contribute_quiz.util

sealed class QuestionsViewMode {
    object Editable : QuestionsViewMode()
    object NonEditable : QuestionsViewMode()
}
