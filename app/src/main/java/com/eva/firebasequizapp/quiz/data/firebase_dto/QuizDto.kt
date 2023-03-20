package com.eva.firebasequizapp.quiz.data.firebase_dto

import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@IgnoreExtraProperties
data class QuizDto(
    @DocumentId val id: String? = null,
    val subject: String = "",
    val desc: String? = null,
    val image: String? = null,
    @ServerTimestamp val lastUpdate: Timestamp? = null,
    val color: String? = null,
    val creatorUID: String? = null
) {
    fun toModel(): QuizModel {
        return QuizModel(
            uid = id?:"",
            subject = subject,
            desc = desc,
            image = image,
            creatorUID = creatorUID,
            lastUpdate = lastUpdate?.let {
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(lastUpdate.seconds), TimeZone
                        .getDefault().toZoneId()
                )
            } ?: LocalDateTime.now(),
            color = color
        )
    }
}
