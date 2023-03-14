package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.QuestionBaseState

@Composable
fun QuestionFields(
    question: CreateQuestionState,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestionViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .padding(PaddingValues(top = 4.dp))
    ) {
        when (question.state) {
            QuestionBaseState.Editable -> {
                TextField(
                    value = question.question,
                    onValueChange = { typedQuestion ->
                        viewModel.onQuestionEvent(
                            CreateQuestionEvent.QuestionQuestionAdded(typedQuestion, question)
                        )
                    },
                    readOnly = question.state != QuestionBaseState.Editable,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(10.dp)
                        ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = 3,
                    placeholder = { Text(text = "This is a question") },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                question.desc?.let {
                    TextField(
                        value = it,
                        onValueChange = { desc ->
                            viewModel.onQuestionEvent(
                                CreateQuestionEvent.DescriptionAdded(
                                    desc,
                                    question
                                )
                            )

                        },
                        readOnly = question.state != QuestionBaseState.Editable,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 4.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        ),
                        maxLines = 3,
                        placeholder = { Text(text = "Add Description") },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
            else -> {
                Text(
                    text = question.question,
                    modifier = Modifier.padding(4.dp,6.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                question.desc?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(4.dp,6.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}