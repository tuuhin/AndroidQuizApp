package com.eva.firebasequizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eva.firebasequizapp.auth.presentation.UserAuthRoute
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.CreateQuestions
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.CreateQuiz
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.quiz.presentation.CurrentQuizRoute
import com.eva.firebasequizapp.quiz.presentation.QuizRoute
import com.eva.firebasequizapp.ui.theme.FirebaseQuizAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


sealed class UserLoginStatus {
    object UserLoggerOut : UserLoginStatus()
    data class UserLoggedIn(val user: FirebaseUser) : UserLoginStatus()
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    private var isAuth: UserLoginStatus = UserLoginStatus.UserLoggerOut

    override fun onStart() {
        isAuth = if (auth.currentUser != null)
            UserLoginStatus.UserLoggedIn(auth.currentUser!!)
        else
            UserLoginStatus.UserLoggerOut
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseQuizAppTheme {
                val navHost = rememberNavController()
                val isAuthenticated = remember { mutableStateOf(isAuth) }
                auth.addAuthStateListener {
                    isAuthenticated.value =
                        if (auth.currentUser != null) UserLoginStatus.UserLoggedIn(auth.currentUser!!)
                        else UserLoginStatus.UserLoggerOut
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (isAuthenticated.value) {
                        UserLoginStatus.UserLoggerOut -> UserAuthRoute()
                        is UserLoginStatus.UserLoggedIn -> {
                            NavHost(
                                navController = navHost,
                                startDestination = NavRoutes.NavHomeRoute.route
                            ) {
                                composable(NavRoutes.NavHomeRoute.route) {
                                    QuizRoute(navController = navHost)
                                }
                                composable(NavRoutes.NavCreateQuizRoute.route) {
                                    CreateQuiz(navController = navHost)
                                }
                                composable(
                                    NavRoutes.NavQuizRoute.route + NavParams.QUIZ_PARAM_ID,
                                    arguments = listOf(navArgument(NavParams.QUIZ_ID) {
                                        type = NavType.StringType
                                    })
                                ) { navStack ->
                                    val quizId =
                                        navStack.arguments?.getString(NavParams.QUIZ_ID) ?: ""
                                    CurrentQuizRoute(navController = navHost, uid = quizId)
                                }
                                composable(
                                    NavRoutes.NavAddQuestionsRoute.route + NavParams.QUIZ_PARAM_ID,
                                    arguments = listOf(navArgument(NavParams.QUIZ_ID) {
                                        type = NavType.StringType
                                    })
                                ) { navStack ->
                                    val quizId =
                                        navStack.arguments?.getString(NavParams.QUIZ_ID) ?: ""
                                    CreateQuestions(uid = quizId, navController = navHost)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
