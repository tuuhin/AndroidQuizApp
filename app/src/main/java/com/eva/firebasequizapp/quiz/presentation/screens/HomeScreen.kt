package com.eva.firebasequizapp.quiz.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.presentation.HomeScreenViewModel
import com.eva.firebasequizapp.quiz.presentation.composables.JoinQuizCard
import com.eva.firebasequizapp.quiz.presentation.composables.QuizResultsCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    val content = viewModel.content.value

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { padding ->
        Column(
            modifier = modifier
                .padding(10.dp)
                .padding(padding)
        ) {
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            JoinQuizCard()
            Column(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {

                Text(
                    text = "Results",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "Your last attempted quizzes results",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (content.isLoading) Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            else if (content.content?.isNotEmpty() == true) {
                LazyColumn {
                    items(content.content.size) { idx ->
                        val data = content.content[idx]
                        data?.let {
                            QuizResultsCard(
                                modifier = Modifier.wrapContentWidth(unbounded = false),
                                result = data
                            )
                        }
                    }
                }
            } else Text(text = "Error")

        }
    }
}