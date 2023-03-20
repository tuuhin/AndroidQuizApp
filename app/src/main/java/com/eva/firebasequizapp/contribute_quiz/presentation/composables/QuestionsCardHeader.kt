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
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.presentation.QuestionBaseState

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
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(0.dp, 4.dp)
        )
        Box (
            modifier = Modifier.fillMaxWidth(.25f)
        ){
            IconButton(onClick = { toggleDropDown = !toggleDropDown }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = "Extra options"
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
                    enabled = question.state == QuestionBaseState.Editable,
                    text = {
                        Text(text = if (question.desc == null) "Add Description" else "Remove Description")
                    },
                    leadingIcon = {
                        Icon(
                            if (question.desc == null) Icons.Default.Add else Icons.Default.Remove,
                            contentDescription = "Add Description"
                        )
                    },
                    onClick = toggleDesc,
                )
                DropdownMenuItem(
                    enabled = question.state == QuestionBaseState.NonEditable,
                    text = { Text(text = "Remove Question") }, onClick = onRemove, leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.RemoveCircleOutline,
                            contentDescription = "Remove Icon"
                        )
                    })
            }
        }
    }
}