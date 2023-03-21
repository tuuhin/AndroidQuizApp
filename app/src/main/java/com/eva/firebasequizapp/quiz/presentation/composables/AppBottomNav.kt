package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val items = remember {
        listOf(TabsInfo.HomeTab, TabsInfo.QuizTab, TabsInfo.ContributionTab, TabsInfo.ProfileTab)
    }
    NavigationBar(
        modifier = modifier
    ) {
        items.forEach { tabs ->
            NavigationBarItem(
                selected = pager.currentPage == tabs.index,
                onClick = {
                    scope.launch { pager.scrollToPage(tabs.index) }
                },
                label = { Text(text = tabs.title) },
                icon = {
                    if (pager.currentPage == tabs.index)
                        Icon(tabs.icon, contentDescription = tabs.desc)
                    else
                        Icon(tabs.outlineIcon, contentDescription = tabs.desc)
                },
            )
        }
    }
}
