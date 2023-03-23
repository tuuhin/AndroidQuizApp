package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuestionState
import com.eva.firebasequizapp.contribute_quiz.util.QuestionsViewMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionCardFooter(
    modifier: Modifier = Modifier,
    questionState: CreateQuestionState,
    onToggle: (Boolean) -> Unit,
    onAnsKey: () -> Unit,
    onDone: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = questionState.required, onCheckedChange = onToggle)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Required")
        }
        when (questionState.state) {
            QuestionsViewMode.Editable -> {
                OutlinedButton(
                    onClick = onAnsKey, shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.FactCheck, contentDescription = "Delete Icon")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "Answer Key")
                }
            }
            QuestionsViewMode.NonEditable -> {
                OutlinedButton(
                    onClick = onDone, shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Done")
                }
            }

        }

    }
}

private class FooterPreviewParams : PreviewParameterProvider<CreateQuestionState> {
    override val values = sequenceOf(
        CreateQuestionState(state = QuestionsViewMode.Editable),
        CreateQuestionState(state = QuestionsViewMode.NonEditable)
    )
}

@Preview
@Composable
private fun QuestionCardFooterPreview(
    @PreviewParameter(FooterPreviewParams::class) state: CreateQuestionState
) {
    QuestionCardFooter(
        questionState = state,
        onToggle = {},
        onAnsKey = { },
        onDone = {}
    )
}