package com.eva.firebasequizapp.quiz.data.firebase_dto

import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@IgnoreExtraProperties
data class QuizDto(
    @DocumentId val id: String = "",
    val subject: String = "",
    val desc: String? = null,
    val image: String? = null,
    @ServerTimestamp val timestamp: Timestamp? = null,
    val color: String? = null,
    val creatorUID: String? = null,
    @PropertyName("approved") val isApproved: Boolean = false
) {
    fun toModel(): QuizModel {
        return QuizModel(uid = id,
            subject = subject,
            desc = desc,
            image = image,
            creatorUID = creatorUID,
            lastUpdate = timestamp?.let {
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(it.seconds), TimeZone.getDefault().toZoneId()
                )
            } ?: LocalDateTime.now(),
            color = color)
    }
}
