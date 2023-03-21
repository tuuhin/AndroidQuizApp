package com.eva.firebasequizapp.quiz.presentation.composables

sealed class QuizArrangementStyle {
    object GridStyle : QuizArrangementStyle()
    object ListStyle : QuizArrangementStyle()
}
