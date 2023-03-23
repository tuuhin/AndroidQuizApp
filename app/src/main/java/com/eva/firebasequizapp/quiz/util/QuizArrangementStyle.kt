package com.eva.firebasequizapp.quiz.util

sealed class QuizArrangementStyle {
    object GridStyle : QuizArrangementStyle()
    object ListStyle : QuizArrangementStyle()
}
