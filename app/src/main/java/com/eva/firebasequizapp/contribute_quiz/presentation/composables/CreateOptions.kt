package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.*

@OptIn(ExperimentalMaterial3Api::class)
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
            Row(
                modifier = when (question.state) {
                    QuestionBaseState.NonEditable -> modifier
                        .fillMaxWidth()
                        .background(
                            color = if (question.ansKey == item)
                                MaterialTheme.colorScheme.secondaryContainer
                            else
                                Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.2f.dp,
                            color = if (question.ansKey == item)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )

                        .clickable {
                            viewModel.onQuestionEvent(
                                CreateQuestionEvent.SetCorrectOption(item, question)
                            )
                        }
                    else -> modifier
                        .fillMaxWidth()
                        .padding(4.dp)

                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                //An icon can be used but the actual thing has a different feeling
                Spacer(
                    modifier = Modifier
                        .width(4.dp)
                        .weight(.025f)
                )
                RadioButton(
                    selected = question.ansKey ==item,
                    onClick = {
                        when (question.state) {
                            QuestionBaseState.NonEditable -> viewModel.onQuestionEvent(
                                CreateQuestionEvent.SetCorrectOption(item, question)
                            )
                            else -> {}
                        }
                    },
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(4.dp,0.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(
                    modifier = Modifier

                        .weight(.025f)
                )
                when (question.state) {
                    QuestionBaseState.Editable -> {
                        OutlinedTextField(
                            value = item.option,
                            onValueChange = { option ->
                                viewModel.onQuestionEvent(
                                    CreateQuestionEvent.OnOptionEvent(
                                        OptionsEvent.OptionValueChanged(
                                            option, index
                                        ), question
                                    )
                                )
                            },
                            maxLines = 2,
                            placeholder = { Text(text = "Option ${index + 1}") },
                            modifier = Modifier
                                .weight(.9f)
                                .fillMaxWidth(),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        viewModel.onQuestionEvent(
                                            CreateQuestionEvent.OnOptionEvent(
                                                OptionsEvent.OptionRemove(index), question
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.RemoveCircleOutline,
                                        contentDescription = "Remove this option"
                                    )
                                }
                            }
                        )
                    }
                    QuestionBaseState.NonEditable -> {
                        Text(
                            text = item.option,
                            modifier = Modifier
                                .weight(.85f)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                    QuestionBaseState.Stale -> {
                        Text(
                            text = item.option, modifier = Modifier
                                .weight(.85f)
                                .fillMaxWidth()
                        )
                    }
                }

            }


        }
    }
}

