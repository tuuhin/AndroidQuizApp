package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizEvents
import com.eva.firebasequizapp.contribute_quiz.presentation.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuiz(
    modifier: Modifier = Modifier, viewModel: QuizViewModel = hiltViewModel()
) {
    val quiz = viewModel.createQuiz.value

    Scaffold(topBar = {
        SmallTopAppBar(title = { Text(text = "Create Quiz") })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Create, contentDescription = "Create Quiz")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Create")
        }
    }) { padding ->
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
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Untitled", style = MaterialTheme.typography.headlineSmall
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall,
                maxLines = 4,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )

            )
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
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Some description", style = MaterialTheme.typography.labelMedium
                    )
                },
                textStyle = MaterialTheme.typography.labelMedium,
                maxLines = 10,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
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