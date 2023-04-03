package com.eva.firebasequizapp.quiz.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.core.composables.NoContentPlaceHolder
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.presentation.HomeScreenViewModel
import com.eva.firebasequizapp.quiz.presentation.composables.JoinQuizCard
import com.eva.firebasequizapp.quiz.presentation.composables.QuizResults
import com.eva.firebasequizapp.quiz.util.DeleteQuizResultsEvent
import com.eva.firebasequizapp.quiz.util.SearchQuizEvents
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                UiEvent.NavigateBack -> {
                    val quizId = viewModel.searchQuizState.value.quizId
                    val route =
                        NavRoutes.NavQuizRoute.route + "/$quizId" + "?${NavParams.SOURCE_VALID_ID}=false"
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        NavParams.QUIZ_TAG,
                        null
                    )
                    navController.navigate(route)
                }
                else -> {}
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->
        val content = viewModel.content.value
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
            JoinQuizCard(
                state = viewModel.searchQuizState.value,
                onQuizIdChange = { viewModel.onQuizSearch(SearchQuizEvents.OnQuizIdChanged(it)) },
                onJoin = { viewModel.onQuizSearch(SearchQuizEvents.Search) },
                onCancel = { viewModel.onQuizSearch(SearchQuizEvents.OnSearchCancelled) },
                onConfirm = { viewModel.onQuizSearch(SearchQuizEvents.OnSearchConfirmed) },
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Results",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = stringResource(id = R.string.results_desc),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (content.isLoading)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                        content = { CircularProgressIndicator() }
                    )
                else if (content.content?.isNotEmpty() == true)
                    QuizResults(
                        content = content.content,
                        state = viewModel.deleteQuizState.value,
                        onDeleteCancelled = {
                            viewModel.onDeleteResult(
                                DeleteQuizResultsEvent.DeleteCanceled
                            )
                        }, onDeleteConfirm = {
                            viewModel.onDeleteResult(DeleteQuizResultsEvent.DeleteConfirmed)
                        }
                    )
                else
                    NoContentPlaceHolder(
                        imageRes = R.drawable.qualification,
                        primaryText = "No Results were found",
                        secondaryText = stringResource(id = R.string.results_not_found),
                        graphicsLayer = {
                            rotationZ = -12.5f
                        }
                    )
            }
        }
    }
}