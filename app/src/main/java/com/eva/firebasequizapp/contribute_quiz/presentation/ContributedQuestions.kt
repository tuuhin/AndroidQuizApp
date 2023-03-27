package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable
import kotlinx.coroutines.flow.collectLatest
import com.eva.firebasequizapp.core.composables.QuizInfoParcelable
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.NoQuestionsFound
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.NonInteractiveQuizCard
import com.eva.firebasequizapp.contribute_quiz.util.QuestionDeleteEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributedQuestions(
    quizId: String,
    parcelable: QuizParcelable?,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: QuestionsViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.errorMessages.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.title)
                else -> {}
            }
        }
    }
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(text = "Quiz Questions") },
            navigationIcon = {
                if (navController.previousBackStackEntry != null) IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to the previous page"
                    )
                }
            },
        )
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                NavParams.QUIZ_TAG,
                parcelable
            )
            navController.navigate(NavRoutes.NavAddQuestionsRoute.route + "/${quizId}")
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add button")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Add Questions")
        }
    }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }

    ) { padding ->
        val content = viewModel.questions.value
        val dialogState = viewModel.deleteDialogState.value

        if (dialogState.isDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onQuestionDelete(
                        QuestionDeleteEvent.CloseDeleteDialog
                    )
                }, confirmButton = {
                    Button(
                        onClick = { viewModel.onQuestionDelete(QuestionDeleteEvent.DeleteConfirmed) },
                        colors = ButtonDefaults
                            .elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                    ) { Text(text = "Delete") }
                }, dismissButton = {
                    TextButton(
                        onClick = { viewModel.onQuestionDelete(QuestionDeleteEvent.CloseDeleteDialog) },
                        colors = ButtonDefaults
                            .textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Cancel")
                    }
                }, title = { Text(text = "Are you sure you wanna delete this") }
            )
        }
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 8.dp)
                .fillMaxSize(),
        ) {
            parcelable?.let { quiz ->
                QuizInfoParcelable(quiz = quiz, showId = false)
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = "All the questions are listed below",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "You can delete or add questions but 😢 cannot update them for now",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (content.isLoading) Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            else if (content.content?.isNotEmpty() == true) LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(content.content) { idx, item ->
                    item?.let { model ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${idx + 1}.",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            NonInteractiveQuizCard(
                                questionModel = model, onDelete = {
                                    viewModel.onQuestionDelete(
                                        QuestionDeleteEvent.QuestionSelected(model)
                                    )
                                }, modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .weight(.8f)
                            )
                        }
                    }
                }
                item {
                    Box(modifier = Modifier.height(50.dp))
                }
            }
            else NoQuestionsFound(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
