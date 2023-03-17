package com.eva.firebasequizapp.profile.presentation

import android.net.Uri

data class UserNameFieldState(
    val name: String = "",
    val error: String? = null,
    val isDialogOpen: Boolean = false,
    val isDismissAllowed: Boolean = false
)

data class ChangeImageState(
    val uri: Uri? = null,
    val isDialogOpen: Boolean = false,
    val isDismissAllowed: Boolean = false
)