package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.eva.firebasequizapp.quiz.presentation.FullQuizViewModel
import com.eva.firebasequizapp.quiz.util.FinalQuizEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterActiveQuizCard(
    quizIndex: Int,
    quiz: QuestionModel,
    modifier: Modifier = Modifier,
    viewModel: FullQuizViewModel = hiltViewModel()
) {
    OutlinedCard(
        modifier = modifier.padding(vertical = 4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(2.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Question: ")
                        }
                        append(quiz.question)
                    }, style = MaterialTheme.typography.titleMedium
                )
                quiz.description?.let { desc ->
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append("Description: ")
                            }
                            append(desc)
                        },
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Divider(modifier = Modifier.padding(vertical = 2.dp), thickness = 1.dp)
            val optionsState = viewModel.optionStates
            quiz.options.forEach { opt ->
                Row(
                    modifier = if (optionsState[quizIndex].option == opt) Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
                        )
                        .border(
                            1.25.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            viewModel.onOptionEvent(
                                FinalQuizEvent.OptionPicked(quizIndex, opt, quiz)
                            )
                        } else Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                        .clickable {
                            viewModel.onOptionEvent(
                                FinalQuizEvent.OptionPicked(quizIndex, opt, quiz)
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = optionsState[quizIndex].option == opt,
                        onClick = {
                            viewModel.onOptionEvent(
                                FinalQuizEvent.OptionPicked(quizIndex, opt, quiz)
                            )
                        },
                        modifier = Modifier.padding(0.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = opt,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}