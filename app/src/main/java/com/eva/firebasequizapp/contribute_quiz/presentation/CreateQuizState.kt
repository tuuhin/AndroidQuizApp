package com.eva.firebasequizapp.contribute_quiz.presentation

import android.net.Uri

data class CreateQuizState(
    val subject: String = "",
    val subjectError: String? = null,
    val desc: String = "",
    val descError: String? = null,
    val image: Uri? = null,
    val createdBy: String? = null
)
