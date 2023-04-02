package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuizEvents
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.QuizColorPicker
import com.eva.firebasequizapp.contribute_quiz.presentation.composables.QuizImagePicker
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuizState
import com.eva.firebasequizapp.core.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuiz(
    navController: NavController,
    state: CreateQuizState,
    showDialog:Boolean,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel()
) {

    val snackBarState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.messagesFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarState.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Create Quiz") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(
                            onClick = navController::navigateUp
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back button"
                            )
                        }
                    }
                })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.onCreateQuizEvent(CreateQuizEvents.OnSubmit) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Quiz")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add")
            }
        }
    ) { padding ->
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = {
                        navController.navigateUp()
                    }) { Text(text = "Ok Got it ", style = MaterialTheme.typography.titleMedium) }
                },
                title = {
                    Text(
                        text = "Quiz Added Successfully",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.create_quiz_desc),
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                })
        }

        Column(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .padding(padding)

        ) {
            Text(
                text = "Contribute A quiz to the app",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.create_quiz_desc),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.subject,
                onValueChange = { sub ->
                    viewModel.onCreateQuizEvent(CreateQuizEvents.OnSubjectChanges(sub))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        shape = MaterialTheme.shapes.medium,
                        color = if (state.subjectError != null) MaterialTheme.colorScheme.error
                        else Color.Transparent
                    ),
                placeholder = {
                    Text(text = "Untitled", style = MaterialTheme.typography.headlineSmall)
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text
                ),
                textStyle = MaterialTheme.typography.headlineSmall,
                maxLines = 4,
                isError = state.subjectError != null,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                shape = MaterialTheme.shapes.medium
            )
            state.subjectError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.desc,
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
                        shape = MaterialTheme.shapes.medium,
                        color = if (state.descError != null) MaterialTheme.colorScheme.error
                        else Color.Transparent
                    ),
                placeholder = { Text(text = "Some description") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text
                ),
                maxLines = 10,
                isError = state.descError != null,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium
            )
            state.descError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            QuizImagePicker()
            QuizColorPicker()
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Normal,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize
                        )
                    ) {
                        append("Note* ")
                    }
                    append(stringResource(id = R.string.create_quiz_info))
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}