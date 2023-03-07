package com.eva.firebasequizapp.auth.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.auth.domain.repository.UserAuthRepository
import com.eva.firebasequizapp.auth.domain.useCases.EmailValidatorUseCase
import com.eva.firebasequizapp.auth.domain.useCases.PasswordValidatorUseCase
import com.eva.firebasequizapp.auth.presentation.UserSignInFormEvents
import com.eva.firebasequizapp.auth.presentation.UserSignInFormState
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSignInViewModel @Inject constructor(
    private val repository: UserAuthRepository,
) : ViewModel() {

    private val signInFlow = MutableSharedFlow<UiEvent>()

    val formState = mutableStateOf(UserSignInFormState())

    val authFlow = signInFlow.asSharedFlow()

    var isLoading = mutableStateOf(false)

    private val emailValidator = EmailValidatorUseCase()
    private val passwordValidator = PasswordValidatorUseCase()

    fun onEvent(event: UserSignInFormEvents) {
        when (event) {
            is UserSignInFormEvents.EmailChanged -> {
                formState.value = formState.value.copy(
                    email = event.email
                )
            }
            UserSignInFormEvents.FormSubmit -> {
                verifyForm()
            }
            is UserSignInFormEvents.PasswordChanged -> {
                formState.value = formState.value.copy(
                    password = event.password
                )
            }
            else -> {}
        }
    }

    private fun verifyForm() {
        val emailValidate = emailValidator.execute(formState.value.email)
        val passwordValidate = passwordValidator.execute(formState.value.password)

        val errors = listOf(emailValidate, passwordValidate).any {
            !it.isValid
        }

        if (errors) {
            formState.value = formState.value.copy(
                emailMessage = emailValidate.message,
                passwordMessage = passwordValidate.message,
            )
            return
        } else {
            formState.value = formState.value.copy(
                emailMessage = null,
                passwordMessage = null,
            )
            signInUser()
        }

    }

    private fun signInUser() {
        viewModelScope.launch {
            val formData = formState.value
            repository.signInUsingEmailAndPassword(formData.email.trim(), formData.password.trim())
                .onEach { res ->
                    when (res) {
                        is Resource.Error -> {
                            isLoading.value = false
                            signInFlow.emit(
                                UiEvent.ShowDialog(
                                    "Authentication Failed",
                                    description = res.message ?: ""
                                )
                            )
                        }
                        is Resource.Loading -> {
                            isLoading.value = true
                        }
                        is Resource.Success -> {
                            isLoading.value = false
                            formState.value = UserSignInFormState()
                        }
                    }
                }
                .launchIn(this)
        }
    }
}