package com.eva.firebasequizapp.core.util

sealed class UiEvent(val title: String, val description: String? = null) {
    data class ShowSnackBar(val message: String) : UiEvent(title = message)
    class ShowDialog(title: String, description: String?) :
        UiEvent(title = title, description = description)
    object NavigateBack : UiEvent(title = "")

}
