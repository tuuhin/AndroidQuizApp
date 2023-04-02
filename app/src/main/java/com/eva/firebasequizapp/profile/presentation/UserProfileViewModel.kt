package com.eva.firebasequizapp.profile.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.auth.domain.repository.UserAuthRepository
import com.eva.firebasequizapp.profile.domain.use_cases.UserNameValidatorUseCase
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.profile.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: UserAuthRepository,
    private val profileRepo: UserProfileRepository,
    val user: FirebaseUser?
) : ViewModel() {

    private val validator = UserNameValidatorUseCase()

    var userNameState = mutableStateOf(UserNameFieldState())

    var profileImageState = mutableStateOf(ChangeImageState())

    private val messagesFlow = MutableSharedFlow<UiEvent>()
    val messages = messagesFlow.asSharedFlow()


    var logoutDialog = mutableStateOf(false)
        private set

    fun onLogoutEvent(event: UserLogoutEvents) {
        when (event) {
            UserLogoutEvents.LogoutButtonClicked -> logoutDialog.value = true

            UserLogoutEvents.LogoutDialogAccepted -> {
                logoutDialog.value = false
                repository.logout()
            }

            UserLogoutEvents.LogoutDialogCanceled -> logoutDialog.value = false

        }
    }


    fun onProfileChangeEvent(event: ChangeImageEvents) {
        when (event) {
            is ChangeImageEvents.PickImage -> {
                profileImageState.value = profileImageState.value.copy(
                    uri = event.uri, isDialogOpen = true, isDismissAllowed = false
                )
            }
            ChangeImageEvents.SubmitChanges -> {
                profileImageState.value = profileImageState.value.copy(
                    isDialogOpen = false
                )
                changeProfileImage(profileImageState.value)

            }
            ChangeImageEvents.ToggleDialog -> {
                profileImageState.value = profileImageState.value.copy(
                    isDialogOpen = !profileImageState.value.isDialogOpen
                )
            }
        }
    }


    fun onChangeNameEvent(event: ChangeNameEvent) {
        when (event) {
            is ChangeNameEvent.NameChanged -> {
                userNameState.value = userNameState.value.copy(
                    name = event.name,
                    error = if (userNameState.value.error != null) null else userNameState.value.error
                )
            }
            ChangeNameEvent.ToggleDialog -> {
                userNameState.value = userNameState.value.copy(
                    isDialogOpen = !userNameState.value.isDismissAllowed, isDismissAllowed = true
                )

            }
            ChangeNameEvent.SubmitRequest -> {
                changeUserName()
                userNameState.value = userNameState.value.copy(
                    isDismissAllowed = false, isDialogOpen = false
                )
            }
        }
    }

    private fun changeUserName() {
        val validate = validator.execute(userNameState.value.name)
        if (validate.isValid) {
            viewModelScope.launch {
                when (val newName = profileRepo.updateUserName(userNameState.value.name)) {
                    is Resource.Success -> {
                        messagesFlow.emit(UiEvent.ShowSnackBar("Your name has been updated"))
                    }
                    is Resource.Error -> {
                        messagesFlow.emit(UiEvent.ShowSnackBar(newName.message ?: "Errors"))
                    }
                    is Resource.Loading ->{
                        messagesFlow.emit(UiEvent.ShowSnackBar("Changing username ...."))
                    }
                }
            }
            return
        }
        userNameState.value = userNameState.value.copy(
            error = validate.message
        )
    }


    private fun changeProfileImage(image: ChangeImageState) {
        if (image.uri != null) {
            viewModelScope.launch {
                when (val newProfile = profileRepo.updateImage(image.uri)) {
                    is Resource.Success -> messagesFlow.emit(
                        UiEvent.ShowSnackBar("Your profile has been  has been updated")
                    )
                    is Resource.Error -> messagesFlow.emit(
                        UiEvent.ShowSnackBar(
                            newProfile.message ?: "Error occurred"
                        )
                    )
                    is Resource.Loading ->{
                        messagesFlow.emit(UiEvent.ShowSnackBar("Setting profile ...."))
                    }
                }
            }
        }
    }
}