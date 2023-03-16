package com.eva.firebasequizapp.contribute_quiz.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ServerTimestamp

data class CreateQuizModel(
    val subject: String = "",
    val desc: String = "",
    val color: String? = null,
    val image: String? = null,
    val createdBy: String? = null,
    val creatorUID: String? = null,
    val questions: CollectionReference? = null,
    @ServerTimestamp val timestamp: Timestamp? = null
)
