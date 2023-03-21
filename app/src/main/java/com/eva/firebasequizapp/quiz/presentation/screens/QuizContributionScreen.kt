package com.eva.firebasequizapp.quiz.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.presentation.ContributionQuizViewModel
import com.eva.firebasequizapp.quiz.presentation.composables.QuizArrangementStyle
import com.eva.firebasequizapp.quiz.presentation.composables.QuizCardGridOrColumn
import com.eva.firebasequizapp.quiz.presentation.composables.QuizTabTitleBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizContributionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ContributionQuizViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.errorFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowDialog -> {}
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.title)
                }
            }
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(NavRoutes.NavCreateQuizRoute.route) }
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Create another quiz")
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Create")
            }
        }) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(10.dp, 0.dp)
                .fillMaxSize()
        ) {
            QuizTabTitleBar(
                title = "Your Contribution",
                arrangementStyle = viewModel.arrangementStyle.value,
                onListStyle = { viewModel.onChangeArrangement(QuizArrangementStyle.ListStyle) },
                onGridStyle = { viewModel.onChangeArrangement(QuizArrangementStyle.GridStyle) }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.contribute_quiz_info),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(PaddingValues(bottom = 2.dp))
            )
            Box(
                modifier = Modifier.weight(.8f)
            ) {
                val quizContent = viewModel.contributedQuizzes.value
                if (quizContent.isLoading)
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                else if (quizContent.content?.isNotEmpty() != null) {
                    val quizzes = quizContent.content
                    QuizCardGridOrColumn(
                        quizzes = quizzes,
                        navController = navController,
                        arrangementStyle = viewModel.arrangementStyle.value
                    )
                } else Text(
                    text = "No quizzes are present",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}