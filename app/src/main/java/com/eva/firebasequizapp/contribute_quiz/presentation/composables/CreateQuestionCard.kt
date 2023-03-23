package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.*
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.util.OptionsEvent
import com.eva.firebasequizapp.contribute_quiz.util.QuestionsViewMode
import com.eva.firebasequizapp.quiz.presentation.composables.AddExtraOptionButton

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
            QuestionCardHeader(index = index, question = question, toggleDesc = {
                viewModel.onQuestionEvent(
                    CreateQuestionEvent.ToggleQuestionDesc(
                        question
                    )
                )
            }, onRemove = {
                viewModel.onQuestionEvent(
                    CreateQuestionEvent.QuestionRemoved(
                        question
                    )
                )
            })
            Divider()
            QuestionFields(question = question)
            CreateOptions(
                question = question,
                questionOptions = question.options
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (question.state ==  QuestionsViewMode.Editable) AddExtraOptionButton(
                onAdd = {
                    viewModel.onQuestionEvent(
                        CreateQuestionEvent.OnOptionEvent(
                            OptionsEvent.OptionAdded, question
                        )
                    )
                },
            )
            Divider()
            QuestionCardFooter(questionState = question, onToggle = {
                viewModel.onQuestionEvent(CreateQuestionEvent.ToggleRequiredField(question))
            }, onAnsKey = {
                viewModel.onQuestionEvent(CreateQuestionEvent.SetNotEditableMode(question))
            }, onDone = {
                viewModel.onQuestionEvent(CreateQuestionEvent.SetEditableMode(question))
            })
        }
    }
}

