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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.NonInteractiveQuizCard
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributedQuestions(
    quizId: String,
    parcelable: QuizParcelable?,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: QuestionsViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel, quizId) {
        viewModel.getCurrentQuizQuestions(quizId)
    }

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
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
        )
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                navController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavParams.QUIZ_TAG, parcelable)
                navController.navigate(NavRoutes.NavAddQuestionsRoute.route + "/${quizId}")
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add button")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Add Questions")
        }
    }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }

    ) { padding ->
        val content = viewModel.questions.value
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 8.dp)
                .fillMaxSize(),
        ) {
            parcelable?.let { quiz ->
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Question Id: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            append(quiz.uid)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
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
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "All the questions are listed below",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = buildAnnotatedString {
                    append("All the")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(" * ")
                    }
                    append("are required question that need to be attempted")
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "You can delete or add questions but 😢 cannot update them for now",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (content.isLoading) {
                    CircularProgressIndicator()
                } else if (content.content?.isNotEmpty() == true) {
                    LazyColumn(
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
                                        modifier = Modifier.weight(.05f),
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    NonInteractiveQuizCard(
                                        questionModel = model,
                                        onDelete = {

                                        },
                                        modifier = Modifier
                                            .padding(vertical = 4.dp)
                                            .weight(.9f)
                                    )
                                }
                            }
                        }
                    }
                } else Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No questions are added the quiz is blank",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Add some questions to make it functional",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}