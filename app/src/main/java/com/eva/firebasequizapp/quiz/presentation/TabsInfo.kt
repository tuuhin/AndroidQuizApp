package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TabsInfo(val title: String, val icon: ImageVector, val desc: String? = null) {
    object HomeTab : TabsInfo(title = "Home", icon = Icons.Default.Home, desc = "Home Tab")
    object QuizTab : TabsInfo(title = "Quiz", icon = Icons.Default.Quiz, desc = "Quiz Tab")
    object ProfileTab : TabsInfo(title = "Profile", icon = Icons.Default.Person, desc = "profile")
}
