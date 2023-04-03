package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
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
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.util.QuestionsViewMode

@Composable
fun QuestionFields(
    question: CreateQuestionState,
    onQuestionChanged: (String) -> Unit,
    onDescChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .padding(vertical = 4.dp)
    ) {
        when (question.state) {
            QuestionsViewMode.Editable -> {
                TextField(
                    value = question.question,
                    onValueChange = onQuestionChanged,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = 3,
                    isError = question.questionError != null,
                    placeholder = { Text(text = "This is a question") },
                    label = { Text(text = "Question") },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                question.questionError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                question.desc?.let {
                    TextField(
                        value = it,
                        onValueChange = onDescChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 4.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        ),
                        maxLines = 3,
                        placeholder = { Text(text = "Add Description") },
                        label = { Text(text = "Question Description") },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }
            else -> {
                Text(
                    text = question.question,
                    modifier = Modifier.padding(4.dp, 6.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                question.desc?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(4.dp, 6.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}