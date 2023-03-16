package com.eva.firebasequizapp.contribute_quiz.presentation

import android.net.Uri

sealed class CreateQuizEvents {
    data class OnSubjectChanges(val subject: String) : CreateQuizEvents()
    data class ObDescChange(val desc: String) : CreateQuizEvents()
    data class OnImageAdded(val uri: Uri? = null) : CreateQuizEvents()

    object OnImageRemoved : CreateQuizEvents()
    data class OnColorAdded(val color: Int? = null) : CreateQuizEvents()

    object OnSubmit : CreateQuizEvents()
}
