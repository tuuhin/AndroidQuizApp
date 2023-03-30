package com.eva.firebasequizapp.quiz.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class FinalQuizState(
    val attemptedCount: Int = 0,
    val optionsState: SnapshotStateList<FinalQuizOptionState> = mutableStateListOf()
)
