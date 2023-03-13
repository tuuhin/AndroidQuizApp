package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizViewModel

@Composable
fun QuestionCardHeader(
    index: Int,
    question:CreateQuestionState,
    modifier: Modifier = Modifier,
    viewModel: CreateQuizViewModel = hiltViewModel()
) {
    var toggleDropDown by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Question ${index + 1}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(0.dp, 4.dp)
        )
        Box {

            IconButton(onClick = { toggleDropDown = !toggleDropDown }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Extra options"
                )
            }
            DropdownMenu(
                expanded = toggleDropDown,
                onDismissRequest = { toggleDropDown = !toggleDropDown },
                properties = PopupProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                ),
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = if (question.desc == null) "Add Description" else "Remove Description")
                    },
                    leadingIcon = {
                        Icon(
                            if (question.desc == null) Icons.Default.Add else Icons.Default.Remove,
                            contentDescription = "Add Description"
                        )
                    },
                    onClick = {
                        viewModel.onQuestionEvent(
                            CreateQuestionEvent.ToggleQuestionDesc(
                                index
                            )
                        )
                    })
//                DropdownMenuItem(
//                    text = { Text(text = "Add Answers") },
//                    onClick = {
//                        viewModel.onQuestionEvent(CreateQuestionEvent.QuestionRemoved(index))
//                    },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.DeleteOutline,
//                            contentDescription = "Remove QQuestion"
//                        )
//                    })
            }
        }
    }
}