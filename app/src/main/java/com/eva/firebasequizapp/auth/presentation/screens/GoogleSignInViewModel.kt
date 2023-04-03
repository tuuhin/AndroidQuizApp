package com.eva.firebasequizapp.auth.presentation.screens

import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.auth.domain.repository.UserAuthRepository
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(
    private val signInClient: SignInClient,
    private val repository: UserAuthRepository
) : ViewModel() {

    private val authFlow = MutableSharedFlow<UiEvent>()

    val googleAuthFlow = authFlow.asSharedFlow()

    var isLoading = mutableStateOf(false)
        private set

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
        try {
            val credentials = signInClient.getSignInCredentialFromIntent(result.data)
            val googleCredentials =
                GoogleAuthProvider.getCredential(credentials.googleIdToken, null)
            googleAuthUsingCredentials(googleCredentials)
        } catch (e: ApiException) {
            e.printStackTrace()
            Log.e("ERROR", e.message ?: "SOME ERROR OCCURRED")
        }
    }


    private fun googleAuthUsingCredentials(credential: AuthCredential) {
        viewModelScope.launch {
            repository.signInWithGoogle(credential).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        isLoading.value = false
                        authFlow.emit(
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
                    }
                }

            }.launchIn(this)
        }
    }
}