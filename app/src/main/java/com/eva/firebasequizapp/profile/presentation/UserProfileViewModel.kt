package com.eva.firebasequizapp.profile.presentation

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.auth.domain.repository.UserAuthRepository
import com.eva.firebasequizapp.auth.domain.useCases.UserNameValidatorUseCase
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.profile.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UserNameDialogState(
    val isDialogOpen: Boolean = false,
    val isDismissAllowed: Boolean = false
)

data class UserNameFieldState(
    val name: String = "",
    val error: String? = null
)

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

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: UserAuthRepository,
    private val profileRepo: UserProfileRepository,
    val user: FirebaseUser?
) : ViewModel() {

    private val validator = UserNameValidatorUseCase()

    var userNameDialog = mutableStateOf(UserNameDialogState())
        private set

    var newUserName = mutableStateOf(UserNameFieldState())


    private val messagesFlow = MutableSharedFlow<UiEvent>()
    val messages = messagesFlow.asSharedFlow()


    var logoutDialog = mutableStateOf(false)
        private set

    fun onLogoutEvent(event: UserLogoutEvents) {
        when (event) {
            UserLogoutEvents.LogoutButtonClicked -> {
                logoutDialog.value = true
            }
            UserLogoutEvents.LogoutDialogAccepted -> {
                logoutDialog.value = false
                repository.logout()
            }
            UserLogoutEvents.LogoutDialogCanceled -> {
                logoutDialog.value = false
            }
        }
    }


    fun onChangeNameEvent(event: ChangeNameEvent) {
        when (event) {
            is ChangeNameEvent.NameChanged -> {
                newUserName.value = newUserName.value.copy(
                    name = event.name
                )
            }
            ChangeNameEvent.ToggleDialog -> {
                userNameDialog.value = userNameDialog.value.copy(
                    isDialogOpen = !userNameDialog.value.isDismissAllowed,
                    isDismissAllowed = true
                )
            }
            ChangeNameEvent.SubmitRequest -> {
                userNameDialog.value = userNameDialog.value.copy(isDismissAllowed = false)
                changeUserName()
            }
        }
    }

    private fun changeUserName() {
        val validate = validator.execute(newUserName.value.name)
        if (false) {
            viewModelScope.launch {
                when (val newName = profileRepo.updateUserName(newUserName.value.name)) {
                    is Resource.Success -> {
                        messagesFlow.emit(UiEvent.ShowSnackBar("Your name has been updated"))
                    }
                    is Resource.Error -> {
                        messagesFlow.emit(UiEvent.ShowSnackBar(newName.message ?: "Errors"))
                    }
                    else -> {}
                }
            }
        } else {
            newUserName.value = newUserName.value.copy(
                error = validate.message
            )
        }
    }


    fun changeProfileImage(image: Uri?) {
        if (image != null) {
            viewModelScope.launch {
                when (val newProfile = profileRepo.updateImage(image)) {
                    is Resource.Success -> {
                        messagesFlow.emit(UiEvent.ShowSnackBar("Your profile has been  has been updated"))

                    }
                    is Resource.Error -> {
                        messagesFlow.emit(
                            UiEvent.ShowSnackBar(
                                newProfile.message ?: "Error occurred"
                            )
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}