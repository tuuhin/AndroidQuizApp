package com.eva.firebasequizapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.core.util.UserLoginStatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    init { getAuthFlow() }

    private val loginFlow = MutableSharedFlow<UserLoginStatus>()

    val authFlow = loginFlow.asSharedFlow()

    private fun getAuthFlow() {
        auth.addAuthStateListener {
            viewModelScope.launch {
                if (it.currentUser == null)
                    loginFlow.emit(UserLoginStatus.UserLoggerOut)
                else
                    loginFlow.emit(UserLoginStatus.UserLoggedIn(it.currentUser))
            }
        }
    }
}