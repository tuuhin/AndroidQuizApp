package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionCard(
    index: Int,
    question: CreateQuestionState,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestionViewModel = hiltViewModel()
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
            Divider()
            QuestionFields(question = question)

            CreateOptions(
                question = question,
                questionOptions = question.options
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (question.state == QuestionBaseState.Editable)
                AddExtraOptionButton(
                    onAdd = {
                        viewModel.onQuestionEvent(
                            CreateQuestionEvent.OnOptionEvent(
                                OptionsEvent.OptionAdded, question
                            )
                        )
                    },
                )
            Divider()
            QuestionCardFooter(
                questionState = question,
                onToggle = {
                    viewModel.onQuestionEvent(CreateQuestionEvent.ToggleRequiredField(question))
                },
                onDelete = {
                    viewModel.onQuestionEvent(CreateQuestionEvent.QuestionRemoved(question))
                },
                onDone = {
                    viewModel.onQuestionEvent(CreateQuestionEvent.SetEditableMode(question))
                }
            )
        }
    }
}

