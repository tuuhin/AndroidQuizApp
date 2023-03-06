package com.eva.firebasequizapp.auth.presentation

import android.provider.Settings.System.getString
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.auth.data.UserAuthRepository
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val repository: UserAuthRepository,
    private val signInClient: SignInClient,
) : ViewModel() {

    private val uiEventFlow = MutableSharedFlow<UiEvent>()

    val events = uiEventFlow.asSharedFlow()

    suspend fun signWithGoogle(clientId: String): Resource<BeginSignInResult> {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(clientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        return try {
            val signInResult = signInClient.beginSignIn(signInRequest).await()
            Resource.Success(signInResult)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "")
        }
    }

    fun activityResolverGoogleSignIn(result: ActivityResult) {
        if (result.resultCode == 0) {
            try {
                val credentials = signInClient.getSignInCredentialFromIntent(result.data)
                val googleCredentials =
                    GoogleAuthProvider.getCredential(credentials.googleIdToken, null)
                googleAuthUsingCredentials(googleCredentials)
            } catch (e: ApiException) {
                Log.e("ERROR", e.message ?: "SOME ERROR OCCURRED")
            }
        }
    }

    private fun googleAuthUsingCredentials(credential: AuthCredential) {
        viewModelScope.launch {
            when (val user = repository.signInWithGoogle(credential)) {
                is Resource.Error -> {
                    uiEventFlow.emit(
                        UiEvent.ShowDialog(
                            title = "Failed",
                            description = user.message ?: "Nothing"
                        )
                    )
                }
                else -> {}
            }
        }
    }

    fun signAnonymously() {
        viewModelScope.launch {
            when (val user = repository.signAnonymously()) {
                is Resource.Success -> {
                    uiEventFlow.emit(UiEvent.ShowSnackBar("LOgged in "))
                }
                is Resource.Error -> {
                    uiEventFlow.emit(
                        UiEvent.ShowDialog(
                            title = "Failed",
                            description = user.message ?: "Nothing"
                        )
                    )
                }
                else -> {

                }
            }
        }
    }

    fun signUpUsingEmailPassword(email: String, password: String,username:String) {
        viewModelScope.launch {
            when (val user = repository.signUpUsingEmailAndPassword(email, password,username)) {
                is Resource.Success -> {

                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }
    }

    fun signInUsingEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            when (val user = repository.signInUsingEmailAndPassword(email, password)) {
                is Resource.Success -> {
                }
                is Resource.Error -> {
                    uiEventFlow.emit(
                        UiEvent.ShowDialog(
                            title = "Error Occurred",
                            description = user.message ?: "Some error"
                        )
                    )
                }
                else -> {}
            }
        }
    }
}