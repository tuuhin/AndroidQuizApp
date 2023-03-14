package com.eva.firebasequizapp.contribute_quiz.presentation

sealed class QuestionBaseState {
    object Editable : QuestionBaseState()
    object NonEditable : QuestionBaseState()
    object Stale : QuestionBaseState()
}
