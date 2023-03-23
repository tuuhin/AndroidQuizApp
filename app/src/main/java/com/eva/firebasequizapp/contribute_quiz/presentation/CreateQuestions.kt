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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.CreateQuestionCard
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
        viewModel.messages.collectLatest { event ->
            when (event) {
                is UiEvent.ShowDialog -> {}
                is UiEvent.ShowSnackBar -> {
                    snackBarState.showSnackbar(event.title)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        topBar = {
            SmallTopAppBar(title = { Text(text = "Add Questions") }, navigationIcon = {
                if (navController.previousBackStackEntry != null) IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow Back"
                    )
                }
            })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                viewModel.onQuestionEvent(
                    CreateQuestionEvent.SubmitQuestions(
                        quizId
                    )
                )
            }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save the questions")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            parcelable?.let { quiz ->
                Text(
                    text = quiz.subject,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                quiz.desc?.let { desc ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 2.dp))
            }
            LazyColumn(
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                itemsIndexed(questions) { index, item ->
                    CreateQuestionCard(index, item)
                }
                item {
                    OutlinedButton(
                        onClick = { viewModel.onQuestionEvent(CreateQuestionEvent.QuestionAdded) },
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .padding(2.dp, 0.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Questions Add")
                        Text(text = "Add Question")
                    }
                }
            }
        }
    }
}