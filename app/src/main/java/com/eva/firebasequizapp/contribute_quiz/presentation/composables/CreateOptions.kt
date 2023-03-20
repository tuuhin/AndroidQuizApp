package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.*

@Composable
fun CreateOptions(
    question: CreateQuestionState,
    questionOptions: MutableList<QuestionOptionsState>,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestionViewModel = hiltViewModel(),
    focusManager: FocusManager = LocalFocusManager.current
) {
    LaunchedEffect(question.state) {
        if (question.state == QuestionBaseState.NonEditable) {
            focusManager.clearFocus()
        }
    }

    Column {
        for ((index, item) in questionOptions.mapIndexed { index, item -> index to item }) {
            CreateOptionBlock(
                optionIndex = index + 1,
                item = item,
                ansKey = question.ansKey,
                state = question.state,
                selectCorrectOption = {
                    viewModel.onQuestionEvent(
                        CreateQuestionEvent.SetCorrectOption(item, question)
                    )
                },
                onOptionRemove = {
                    viewModel.onQuestionEvent(
                        CreateQuestionEvent.OnOptionEvent(
                            OptionsEvent.OptionRemove(item), question
                        )
                    )
                },
                onOptionValueChange = { option ->
                    viewModel.onQuestionEvent(
                        CreateQuestionEvent.OnOptionEvent(
                            OptionsEvent.OptionValueChanged(
                                option, index
                            ), question
                        )
                    )
                },
                modifier = modifier
            )
        }
    }
}

