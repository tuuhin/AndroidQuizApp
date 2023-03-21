package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TabsInfo(
    val title: String,
    val icon: ImageVector,
    val outlineIcon: ImageVector,
    val desc: String? = null,
    val index: Int
) {
    object HomeTab : TabsInfo(
        title = "Home",
        icon = Icons.Default.Home,
        outlineIcon = Icons.Outlined.Home,
        desc = "Home Tab",
        index = 0
    )

    object QuizTab : TabsInfo(
        title = "Quiz",
        icon = Icons.Default.Feedback,
        outlineIcon = Icons.Outlined.Feedback,
        desc = "Quiz Tab",
        index = 1
    )

    object ContributionTab : TabsInfo(
        title = "Contribution",
        icon = Icons.Default.AddCircleOutline,
        outlineIcon = Icons.Outlined.AddCircleOutline,
        desc = "Contribution Tab",
        index = 2
    )

    object ProfileTab : TabsInfo(
        title = "Profile",
        icon = Icons.Default.Person,
        outlineIcon = Icons.Outlined.Person,
        desc = "profile",
        index = 3
    )
}
