package com.eva.firebasequizapp.quiz.util

import com.eva.firebasequizapp.quiz.domain.models.QuizModel

sealed class QuizInteractionEvents {
    data class QuizSelected(val quiz: QuizModel) : QuizInteractionEvents()
    object QuizUnselect : QuizInteractionEvents()
}