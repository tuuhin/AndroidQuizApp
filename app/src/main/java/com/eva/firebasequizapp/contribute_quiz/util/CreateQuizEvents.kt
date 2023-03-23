package com.eva.firebasequizapp.contribute_quiz.util

import android.net.Uri

sealed class CreateQuizEvents {
    data class OnSubjectChanges(val subject: String) : CreateQuizEvents()
    data class ObDescChange(val desc: String) : CreateQuizEvents()
    data class OnImageAdded(val uri: Uri? = null) : CreateQuizEvents()

    object OnImageRemoved : CreateQuizEvents()
    data class OnColorAdded(val color: ULong? = null) : CreateQuizEvents()

    object OnSubmit : CreateQuizEvents()
}
