package com.eva.firebasequizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.auth.presentation.UserAuthRoute
import com.eva.firebasequizapp.core.util.UserLoginStatus
import com.eva.firebasequizapp.ui.theme.FirebaseQuizAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    private var status: UserLoginStatus = UserLoginStatus.UserLoggerOut

    override fun onStart() {
        status = when (auth.currentUser) {
            is FirebaseUser -> UserLoginStatus.UserLoggedIn(auth.currentUser)
            else -> UserLoginStatus.UserLoggerOut
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseQuizAppTheme {
                val auth = hiltViewModel<UserAuthViewModel>()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state =
                        auth.authFlow.collectAsState(
                            initial = status,
                            context = Dispatchers.Main
                        )
                    when (state.value) {
                        UserLoginStatus.UserLoggerOut -> UserAuthRoute()
                        is UserLoginStatus.UserLoggedIn -> AppRoutes()
                    }
                }
            }
        }
    }
}
