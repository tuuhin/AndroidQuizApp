package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.presentation.QuestionBaseState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionCardFooter(
    modifier: Modifier = Modifier,
    questionState: CreateQuestionState,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onDone: () -> Unit
) {
    when (questionState.state) {
        QuestionBaseState.Editable -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(2.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = questionState.required,
                        onCheckedChange = onToggle
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Required")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Icon"
                    )
                }
            }
        }
        QuestionBaseState.NonEditable -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(2.dp), horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onDone) {
                    Text(text = "Done")
                }
            }
        }
        QuestionBaseState.Stale -> {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(10.dp))
        }
    }

}