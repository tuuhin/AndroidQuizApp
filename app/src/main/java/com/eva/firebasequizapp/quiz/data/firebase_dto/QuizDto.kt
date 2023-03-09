package com.eva.firebasequizapp.quiz.data.firebase_dto

import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@IgnoreExtraProperties
data class QuizDto(
    val id: String = "",
    val subject: String = "",
    val desc: String? = null,
    val image: String? = null,
    val createdBy: String = "",
    val lastUpdate: Timestamp? = null,
) {
    fun toModel(): QuizModel {
        return QuizModel(
            subject = subject,
            subjectDescription = desc,
            associatedImage = image,
            createdBy = createdBy,
            lastUpdate = lastUpdate?.let {
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(lastUpdate.seconds), TimeZone
                        .getDefault().toZoneId()
                )
            } ?: LocalDateTime.now(),
        )
    }
}
