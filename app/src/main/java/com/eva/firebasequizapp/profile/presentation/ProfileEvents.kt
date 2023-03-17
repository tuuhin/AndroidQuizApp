package com.eva.firebasequizapp.profile.presentation

import android.net.Uri

sealed class ChangeNameEvent {
    object SubmitRequest : ChangeNameEvent()
    data class NameChanged(val name: String) : ChangeNameEvent()
    object ToggleDialog : ChangeNameEvent()
}

sealed class UserLogoutEvents {
    object LogoutButtonClicked : UserLogoutEvents()
    object LogoutDialogCanceled : UserLogoutEvents()
    object LogoutDialogAccepted : UserLogoutEvents()
}

sealed class ChangeImageEvents {
    object SubmitChanges : ChangeImageEvents()
    data class PickImage(val uri: Uri) : ChangeImageEvents()
    object ToggleDialog : ChangeImageEvents()
}