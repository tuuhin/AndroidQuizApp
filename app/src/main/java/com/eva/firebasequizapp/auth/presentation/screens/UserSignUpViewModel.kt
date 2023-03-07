package com.eva.firebasequizapp.auth.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.auth.domain.repository.UserAuthRepository
import com.eva.firebasequizapp.auth.domain.useCases.EmailValidatorUseCase
import com.eva.firebasequizapp.auth.domain.useCases.PasswordValidatorUseCase
import com.eva.firebasequizapp.auth.domain.useCases.UserNameValidatorUseCase
import com.eva.firebasequizapp.auth.presentation.auth_form_state.UserSignUpFormEvent
import com.eva.firebasequizapp.auth.presentation.auth_form_state.UserSignUpFormState
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSignUpViewModel @Inject constructor(
    private val repository: UserAuthRepository,
) : ViewModel() {

    private val emailValidator = EmailValidatorUseCase()
    private val passwordValidator = PasswordValidatorUseCase()
    private val usernameValidator = UserNameValidatorUseCase()

    var isLoading = mutableStateOf(false)
        private set

    var formState = mutableStateOf(UserSignUpFormState())
        private set

    private val signUpFlow = MutableSharedFlow<UiEvent>()

    val authFlow = signUpFlow.asSharedFlow()

    fun onEvent(event: UserSignUpFormEvent) {
        when (event) {
            is UserSignUpFormEvent.EmailChanged -> {
                formState.value = formState.value.copy(email = event.email)
            }
            is UserSignUpFormEvent.PasswordChanged -> {
                formState.value = formState.value.copy(password = event.password)
            }
            UserSignUpFormEvent.Submit -> {
                submitForm()
            }
            is UserSignUpFormEvent.UsernameChanged -> {
                formState.value = formState.value.copy(username = event.username)
            }
        }
    }

    private fun submitForm() {
        val emailValidate = emailValidator.execute(formState.value.email)
        val passwordValidate = passwordValidator.execute(formState.value.password)
        val usernameValidate = usernameValidator.execute(formState.value.username)

        val errors = listOf(emailValidate, passwordValidate, usernameValidate).any {
            !it.isValid
        }

        if (errors) {
            formState.value = formState.value.copy(
                emailMessage = emailValidate.message,
                passwordMessage = passwordValidate.message,
                usernameMessage = usernameValidate.message
            )
            return
        } else {
            formState.value = formState.value.copy(
                emailMessage = null,
                passwordMessage = null,
                usernameMessage = null
            )
            signUpUser()
        }
    }


    private fun signUpUser() {
        viewModelScope.launch {
            val formData = formState.value
            repository.signUpUsingEmailAndPassword(
                formData.email.trim(),
                formData.password.trim(),
            ).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        isLoading.value = false
                        signUpFlow.emit(
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
                        formState.value = UserSignUpFormState()
                    }
                }
            }.launchIn(this)
        }
    }


}