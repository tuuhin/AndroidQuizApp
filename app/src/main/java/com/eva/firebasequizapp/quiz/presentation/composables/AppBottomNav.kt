package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.eva.firebasequizapp.quiz.presentation.TabsInfo
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppBottomNav(
    pager: PagerState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = pager.currentPage == 0,
            onClick = {
                scope.launch { pager.animateScrollToPage(0) }
            },
            label = { Text(text = TabsInfo.HomeTab.title) },
            icon = {
                Icon(TabsInfo.HomeTab.icon, contentDescription = TabsInfo.HomeTab.desc)
            },
        )
        NavigationBarItem(
            selected = pager.currentPage == 1,
            onClick = {
                scope.launch { pager.animateScrollToPage(1) }
            },
            label = { Text(text = TabsInfo.QuizTab.title) },
            icon = {
                Icon(
                    TabsInfo.QuizTab.icon, contentDescription = TabsInfo.QuizTab.desc
                )
            },
        )
        NavigationBarItem(
            selected = pager.currentPage == 2,
            onClick = {
                scope.launch { pager.animateScrollToPage(2) }
            },
            label = { Text(text = TabsInfo.ProfileTab.title) },
            icon = {
                Icon(
                    TabsInfo.ProfileTab.icon, contentDescription = TabsInfo.ProfileTab.desc
                )
            },
        )
    }
}
