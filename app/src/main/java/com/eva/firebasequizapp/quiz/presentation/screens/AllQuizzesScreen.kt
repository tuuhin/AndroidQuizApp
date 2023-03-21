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
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.presentation.AllQuizzesViewModel
import com.eva.firebasequizapp.quiz.presentation.QuizInteractionEvents
import com.eva.firebasequizapp.quiz.presentation.composables.QuizArrangementStyle
import com.eva.firebasequizapp.quiz.presentation.composables.QuizCardGridOrColumn
import com.eva.firebasequizapp.quiz.presentation.composables.QuizTabTitleBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllQuizzesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AllQuizzesViewModel = hiltViewModel(),
) {
    val showDialog = viewModel.showDialog.value
    val selectedQuiz = viewModel.dialogContent.value

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.errorFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.title)
                }
                else -> {}
            }
        }
    }

    if (showDialog && selectedQuiz != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(QuizInteractionEvents.QuizUnselect) },
            confirmButton = {
                Button(onClick = {}) {
                    Text(text = "OK start the quiz")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(QuizInteractionEvents.QuizUnselect) }) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = selectedQuiz.subject) },
            text = { Text(text = "Start this quiz") },
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->

        Column(
            modifier = modifier
                .padding(10.dp, 0.dp)
                .padding(padding)
                .fillMaxSize()
        ) {
            QuizTabTitleBar(
                title = "Your Quizzes",
                arrangementStyle = viewModel.arrangementStyle.value,
                onListStyle = { viewModel.onArrangementChange(QuizArrangementStyle.ListStyle) },
                onGridStyle = { viewModel.onArrangementChange(QuizArrangementStyle.GridStyle) }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.quiz_tab_info),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Box(modifier = Modifier.weight(.8f)) {
                val quizContent = viewModel.quizzes.value
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