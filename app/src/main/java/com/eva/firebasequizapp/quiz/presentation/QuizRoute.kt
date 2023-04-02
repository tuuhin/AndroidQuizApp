package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.profile.presentation.UserProfileViewModel
import com.eva.firebasequizapp.profile.presentation.composables.FireBaseUserTopBar
import com.eva.firebasequizapp.quiz.presentation.composables.AppBottomNav
import com.eva.firebasequizapp.quiz.presentation.screens.AllQuizzesScreen
import com.eva.firebasequizapp.quiz.presentation.screens.HomeScreen
import com.eva.firebasequizapp.quiz.presentation.screens.ProfileScreen
import com.eva.firebasequizapp.quiz.presentation.screens.QuizContributionScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun QuizRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val pager = rememberPagerState(initialPage = 0)

    Scaffold(
        topBar = { FireBaseUserTopBar(pager = pager, user = viewModel.user) },
        bottomBar = { AppBottomNav(pager = pager) },
    ) { padding ->
        HorizontalPager(
            count = 4,
            state = pager,
            contentPadding = padding,
            modifier = modifier,
        ) { idx ->
            when (idx) {
                0 -> HomeScreen(navController = navController)
                1 -> AllQuizzesScreen(navController = navController)
                2 -> QuizContributionScreen(navController = navController)
                3 -> ProfileScreen()
            }
        }
    }
}
