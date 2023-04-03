package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.util.QuestionsViewMode

@Composable
fun QuestionCardHeader(
    index: Int,
    question: CreateQuestionState,
    modifier: Modifier = Modifier,
    toggleDesc: () -> Unit,
    onRemove: () -> Unit
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
            modifier = Modifier.padding(vertical =  4.dp)
        )
        Box(
            modifier = Modifier.fillMaxWidth(.25f)
        ) {
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
                    enabled = question.state == QuestionsViewMode.Editable,
                    text = {
                        if (question.desc == null)
                            Text(text = "Add Description")
                        else
                            Text(text = "Remove Description")
                    },
                    leadingIcon = {
                        if (question.desc == null)
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Description"
                            )
                        else Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove Description"
                        )
                    },
                    onClick = toggleDesc,
                )
                DropdownMenuItem(
                    enabled = question.isDeleteAllowed,
                    text = { Text(text = "Remove Question") },
                    onClick = onRemove,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.RemoveCircleOutline,
                            contentDescription = "Remove Icon"
                        )
                    }
                )
            }
        }
    }
}