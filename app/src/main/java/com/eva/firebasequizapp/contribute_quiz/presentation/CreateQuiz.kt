package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuizEvents
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.QuizColorPicker
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.QuizImagePicker
import com.eva.firebasequizapp.core.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuiz(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val quiz = viewModel.createQuiz.value

    val snackBarState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarState) }, topBar = {
        SmallTopAppBar(title = { Text(text = "Create Quiz") }, navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { viewModel.onCreateQuizEvent(CreateQuizEvents.OnSubmit) }) {
            Icon(imageVector = Icons.Default.Create, contentDescription = "Create Quiz")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Create")
        }
    }) { padding ->
        LaunchedEffect(viewModel) {
            viewModel.messagesFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> {
                        snackBarState.showSnackbar(event.title)
                    }
                    else -> {}
                }
            }
        }

        Column(
            modifier = modifier
                .padding(10.dp)
                .padding(padding)
        ) {
            TextField(
                value = quiz.subject,
                onValueChange = { sub ->
                    viewModel.onCreateQuizEvent(CreateQuizEvents.OnSubjectChanges(sub))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = if (quiz.subjectError != null) MaterialTheme.colorScheme.error else Color.Transparent
                    ),
                placeholder = {
                    Text(text = "Untitled", style = MaterialTheme.typography.headlineSmall)
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                textStyle = MaterialTheme.typography.headlineSmall,
                maxLines = 4,
                isError = quiz.subjectError != null,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),
                shape = RoundedCornerShape(10.dp)
            )
            quiz.subjectError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = quiz.desc,
                onValueChange = { desc ->
                    viewModel.onCreateQuizEvent(
                        CreateQuizEvents.ObDescChange(
                            desc
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = if (quiz.descError != null) MaterialTheme.colorScheme.error else Color.Transparent
                    ),
                placeholder = { Text(text = "Some description") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text
                ),
                maxLines = 10,
                isError = quiz.descError != null,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp)
            )
            quiz.descError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            QuizImagePicker()
            QuizColorPicker()
            quiz.createdBy?.let { createdBy ->
                Text(
                    text = buildAnnotatedString {
                        append("Created by: ")
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append(createdBy)
                        }
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

    }
}