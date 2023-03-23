package com.eva.firebasequizapp.quiz.data.parcelable

import android.os.Parcelable
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizParcelable(
    val uid: String = "",
    val subject: String = "",
    val desc: String? = null,
) : Parcelable

fun QuizModel.toParcelable(): QuizParcelable {
    return QuizParcelable(
        uid = uid,
        desc = desc,
        subject = subject
    )
}