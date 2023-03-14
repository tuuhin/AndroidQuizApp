package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.QuestionBaseState

@Composable
fun QuestionCardHeader(
    index: Int,
    question: CreateQuestionState,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestionViewModel = hiltViewModel()
) {
    var toggleDropDown by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Question ${index + 1}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(0.dp, 4.dp)
        )
        Box {

            IconButton(
                onClick = { toggleDropDown = !toggleDropDown }
            ) {
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
                                question
                            )
                        )
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = "Add Image") },
                    onClick = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Image, contentDescription = "Add Image"
                        )
                    },
                )
                DropdownMenuItem(
                    enabled = question.state != QuestionBaseState.Editable,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle Options"
                        )
                    },
                    text = { Text(text = "Shuffle Options") },
                    onClick = {
                        if (question.state != QuestionBaseState.Editable)
                            viewModel.onQuestionEvent(CreateQuestionEvent.ShuffleOptions(question))
                    })
                DropdownMenuItem(
                    text = { Text(text = "Answers Key") }, onClick = {
                        viewModel.onQuestionEvent(CreateQuestionEvent.SetNotEditableMode(question))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.FactCheck,
                            contentDescription = "Add AnswerKey"
                        )
                    }, colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )

            }
        }
    }
}