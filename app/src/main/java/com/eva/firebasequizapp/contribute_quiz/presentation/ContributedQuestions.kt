package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable
import kotlinx.coroutines.flow.collectLatest
import com.eva.firebasequizapp.core.composables.QuizInfoParcelable
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.NonInteractiveQuizCard
import com.eva.firebasequizapp.contribute_quiz.util.DeleteQuestionsState
import com.eva.firebasequizapp.contribute_quiz.util.DeleteQuizEvents
import com.eva.firebasequizapp.contribute_quiz.util.DeleteWholeQuizState
import com.eva.firebasequizapp.contribute_quiz.util.QuestionDeleteEvent
import com.eva.firebasequizapp.core.composables.NoContentPlaceHolder
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributedQuestions(
    quizId: String,
    parcelable: QuizParcelable?,
    navController: NavController,
    questionsState: ShowContent<List<QuestionModel?>>,
    deleteQuizState: DeleteWholeQuizState,
    deleteQuestionState: DeleteQuestionsState,
    modifier: Modifier = Modifier,
    viewModel: QuestionsViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }

    var isDropDownOpen by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.errorMessages
            .collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                    UiEvent.NavigateBack -> navController.navigateUp()
                    else -> {}
                }
            }
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Quiz Questions") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null)
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to the previous page"
                            )
                        }
                },
                actions = {
                    IconButton(
                        onClick = { isDropDownOpen = !isDropDownOpen }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                    DropdownMenu(
                        expanded = isDropDownOpen,
                        onDismissRequest = { isDropDownOpen = !isDropDownOpen }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Delete") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Icon"
                                )
                            },
                            onClick = {
                                isDropDownOpen = !isDropDownOpen
                                viewModel.onDeleteQuizEvent(DeleteQuizEvents.PickQuiz(quizId))
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController
                        .currentBackStackEntry
                        ?.savedStateHandle?.set(NavParams.QUIZ_TAG, parcelable)
                    navController.navigate(NavRoutes.NavAddQuestionsRoute.route + "/${quizId}")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add button")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add Questions")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }

    ) { padding ->

        if (deleteQuizState.showDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onDeleteQuizEvent(DeleteQuizEvents.OnDeleteCanceled) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.onDeleteQuizEvent(DeleteQuizEvents.OnDeleteConfirmed) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(text = "Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.onDeleteQuizEvent(DeleteQuizEvents.OnDeleteCanceled) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Do you want to delete this quiz") },
                text = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (deleteQuizState.isDeleting) {
                            Text(
                                text = "Deleting please wait",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.delete_full_quiz_desc),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            )
        }
        if (deleteQuestionState.isDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onQuestionDelete(QuestionDeleteEvent.CloseDeleteDialog)
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.onQuestionDelete(QuestionDeleteEvent.DeleteConfirmed) },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) { Text(text = "Delete") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.onQuestionDelete(QuestionDeleteEvent.CloseDeleteDialog) },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Are you sure you wanna delete this") }
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
                text = stringResource(id = R.string.list_questions_title),
                style = MaterialTheme.typography.titleSmall,
                lineHeight = 12.sp
            )
            Text(
                text = stringResource(id = R.string.list_questions_addtional),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (questionsState.isLoading) Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            else if (questionsState.content?.isNotEmpty() == true) LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(questionsState.content) { idx, item ->
                    item?.let { model ->
                        Row(
                            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
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
            else NoContentPlaceHolder(primaryText = "No questions found",
                imageRes = R.drawable.confused,
                secondaryText = "No questions are added the quiz is blank",
                graphicsLayer = {
                    rotationX = -10f
                })
        }
    }
}
