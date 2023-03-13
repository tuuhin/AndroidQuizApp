package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.OptionsEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionCard(
    index: Int,
    question: CreateQuestionState,
    modifier: Modifier = Modifier,
    viewModel: CreateQuizViewModel = hiltViewModel()
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            QuestionCardHeader(index = index, question = question)
            TextField(
                value = question.question,
                onValueChange = {
                    viewModel.onQuestionEvent(
                        CreateQuestionEvent.QuestionQuestionAdded(it, index)
                    )
                },
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
                        viewModel.onQuestionEvent(CreateQuestionEvent.DescriptionAdded(desc, index))

                    },
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
            CreateOptions(
                questionIndex = index,
                questionOptions = question.options
            )
            Spacer(modifier = Modifier.height(2.dp))
            AddExtraOptionButton(onAdd = {
                viewModel.onQuestionEvent(
                    CreateQuestionEvent.OnOptionEvent(
                        OptionsEvent.OptionAdded, index
                    )
                )
            })
            Divider()
            QuestionCardFooter(
                questionState = question,
                onToggle = {
                    viewModel.onQuestionEvent(CreateQuestionEvent.ToggleRequiredField(index))
                }, onDelete = {
                    viewModel.onQuestionEvent(CreateQuestionEvent.QuestionRemoved(index))
                }
            )
        }
    }
}

