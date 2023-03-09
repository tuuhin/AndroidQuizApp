package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.profile.presentation.composables.FireBaseUserTopBar
import com.eva.firebasequizapp.quiz.presentation.composables.AppBottomNav
import com.eva.firebasequizapp.quiz.presentation.screens.AllQuizzesScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun QuizRoute(
    modifier: Modifier = Modifier,
) {
    val pager = rememberPagerState()
    Scaffold(
        topBar = { FireBaseUserTopBar() },
        bottomBar = { AppBottomNav(pager = pager) }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalPager(count = 3) {
                AllQuizzesScreen()
            }
        }
    }
}