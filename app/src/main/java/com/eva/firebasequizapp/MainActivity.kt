package com.eva.firebasequizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.eva.firebasequizapp.auth.presentation.UserAuthRoute
import com.eva.firebasequizapp.quiz.presentation.QuizRoute
import com.eva.firebasequizapp.ui.theme.FirebaseQuizAppTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseQuizAppTheme {
                var isAuthenticated by remember { mutableStateOf(false) }
                auth.addAuthStateListener { isAuthenticated = it.currentUser != null }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isAuthenticated) {
                        QuizRoute()
                    } else
                        UserAuthRoute()
                }

            }
        }
    }
}
