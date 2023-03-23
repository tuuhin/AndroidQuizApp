package com.eva.firebasequizapp.contribute_quiz.util

import android.net.Uri

data class CreateQuizState(
    val subject: String = "",
    val subjectError: String? = null,
    val desc: String = "",
    val color: ULong? = null,
    val descError: String? = null,
    val image: Uri? = null,
    val createdBy: String? = null,
    val creatorUID: String? = null
)
