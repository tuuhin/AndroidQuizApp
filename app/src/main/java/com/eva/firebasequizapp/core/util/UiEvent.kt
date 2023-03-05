package com.eva.firebasequizapp.core.util

sealed class UiEvent(val title: String, val description: String? = null) {
    class ShowSnackBar(message: String) : UiEvent(title = message)
    class ShowDialog(title: String, description: String) :
        UiEvent(title = title, description = description)
}
