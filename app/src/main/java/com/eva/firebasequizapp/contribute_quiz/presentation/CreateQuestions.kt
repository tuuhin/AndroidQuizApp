package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.CreateQuestionCard
import com.eva.firebasequizapp.core.composables.QuizInfoParcelable
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestions(
    quizId: String,
    parcelable: QuizParcelable?,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestionViewModel = hiltViewModel()
) {
    val questions = viewModel.questions

    val snackBarState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarState.showSnackbar(event.message)
                UiEvent.NavigateBack -> navController.navigateUp()
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Add Questions") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null)
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Arrow Back"
                            )
                        }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.onQuestionEvent(CreateQuestionEvent.SubmitQuestions(quizId))
                }
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save the questions")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Save")
            }
        }
    ) { padding ->
        if (viewModel.showDialog.value)
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Adding Questions", textAlign = TextAlign.Center) },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.adding_question_wait),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                },
                confirmButton = {}
            )
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            parcelable?.let { quiz ->
                QuizInfoParcelable(quiz = quiz, showId = false)
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = stringResource(id = R.string.add_quiz_question_text),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            LazyColumn(
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                itemsIndexed(questions) { index, item ->
                    CreateQuestionCard(index, item)
                }
                item {
                    OutlinedButton(
                        onClick = {
                            viewModel.onQuestionEvent(CreateQuestionEvent.QuestionAdded)
                        },
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .padding(horizontal = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add another question"
                        )
                        Text(text = "Add Question")
                    }
                }
                item {
                    Box(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}