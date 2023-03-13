package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.OptionsEvent
import com.eva.firebasequizapp.quiz.domain.models.QuestionOptionsModel

@Composable
fun CreateOptions(
    questionIndex: Int,
    questionOptions: MutableList<QuestionOptionsModel>,
    modifier: Modifier = Modifier,
    viewModel: CreateQuizViewModel = hiltViewModel()
) {
    Column {
        for ((index, item) in questionOptions.mapIndexed { index, item -> index to item }) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Options Mode",
                    modifier = Modifier.weight(.05f)
                )
                Spacer(
                    modifier = Modifier
                        .width(4.dp)
                        .weight(.05f)
                )
                OutlinedTextField(
                    value = item.option,
                    onValueChange = { option ->
                        viewModel.onQuestionEvent(
                            CreateQuestionEvent.OnOptionEvent(
                                OptionsEvent.OptionValueChanged(
                                    option, index
                                ), questionIndex
                            )
                        )
                    },
                    maxLines = 2,
                    placeholder = { Text(text = "Option ${questionIndex + 1}") },
                    modifier = Modifier
                        .weight(.9f)
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onQuestionEvent(
                                    CreateQuestionEvent.OnOptionEvent(
                                        OptionsEvent.OptionRemove(index), questionIndex
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


        }
    }
}

