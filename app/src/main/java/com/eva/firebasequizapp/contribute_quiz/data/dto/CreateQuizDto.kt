package com.eva.firebasequizapp.contribute_quiz.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class CreateQuizDto(
    val subject: String = "",
    val desc: String = "",
    val color: String? = null,
    val image: String? = null,
    val creatorUID: String ,
    @ServerTimestamp val timestamp: Timestamp? = null
)