package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.contribute_quiz.util.QuestionOptionsState
import com.eva.firebasequizapp.contribute_quiz.util.QuestionsViewMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOptionBlock(
    optionIndex: Int,
    item: QuestionOptionsState,
    state: QuestionsViewMode,
    modifier: Modifier = Modifier,
    ansKey: QuestionOptionsState? = null,
    selectCorrectOption: () -> Unit,
    onOptionRemove: () -> Unit,
    onOptionValueChange: (String) -> Unit,
) {
    Row(
        modifier = when (state) {
            QuestionsViewMode.NonEditable -> modifier
                .fillMaxWidth()
                .background(
                    color = if (ansKey == item)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        Color.Transparent,
                    shape = MaterialTheme.shapes.medium
                )
                .border(
                    1.2f.dp,
                    color = if (ansKey == item)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Transparent,
                    shape = MaterialTheme.shapes.medium
                )
                .clickable(onClick = selectCorrectOption)
            else -> modifier
                .fillMaxWidth()
                .padding(4.dp)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        //An icon can be used but the actual thing has a different feeling,
        // I meant this for the radio button 😊
        Spacer(
            modifier = Modifier
                .width(4.dp)
                .weight(.025f)
        )
        RadioButton(
            selected = ansKey == item,
            onClick = when (state) {
                QuestionsViewMode.NonEditable -> selectCorrectOption
                else -> {
                    {}
                }
            },
            modifier = Modifier
                .weight(0.1f)
                .padding(4.dp, 0.dp),
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.weight(.025f))
        when (state) {
            QuestionsViewMode.Editable -> {
                Column(
                    modifier = Modifier.weight(.9f)
                ) {
                    OutlinedTextField(
                        value = item.option,
                        onValueChange = onOptionValueChange,
                        maxLines = 1,
                        placeholder = { Text(text = "Option $optionIndex") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = onOptionRemove) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove this option"
                                )
                            }
                        }
                    )
                    item.optionError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            else -> {
                Text(
                    text = item.option, modifier = Modifier
                        .weight(.85f)
                        .fillMaxWidth()
                )
            }
        }
    }
}
