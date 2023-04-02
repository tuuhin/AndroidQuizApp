package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.quiz.util.SearchQuizState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinQuizCard(
    state: SearchQuizState,
    onQuizIdChange: (String) -> Unit,
    onJoin: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (state.showDialog)
        AlertDialog(
            onDismissRequest = onCancel,
            dismissButton = {
                TextButton(onClick = onCancel) { Text(text = "Cancel") }
            },
            confirmButton = {
                Button(onClick = onConfirm) { Text(text = "Join") }
            },
            title = { Text(text = "Join this quiz") },
            text = { Text(text = "Do you want to join the quiz with ${state.quizId}") }
        )

    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Quiz Code",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(id = R.string.home_tab_card_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = state.quizId,
                label = { Text(text = "Uid") },
                placeholder = { Text(text = "Ex: 67dHw.....") },
                onValueChange = onQuizIdChange,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                isError = state.quizIdError != null,
                maxLines = 1,
            )
            state.quizIdError?.let { err ->
                Text(
                    text = err,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Button(
                onClick = onJoin,
                modifier = Modifier.align(Alignment.End),
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Join the quiz"
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "Join")
            }
        }
    }
}

private class SearchQuizStates : PreviewParameterProvider<SearchQuizState> {
    override val values: Sequence<SearchQuizState> = sequenceOf(
        SearchQuizState(showDialog = true, quizId = "67dHw....."),
        SearchQuizState(showDialog = false),
        SearchQuizState(
            showDialog = false,
            quizIdError = "Cannot use this value"
        )
    )

}

@Composable
@Preview
private fun JoinQuizCardPreview(
    @PreviewParameter(SearchQuizStates::class) state: SearchQuizState
) {
    JoinQuizCard(
        state = state,
        onQuizIdChange = {},
        onJoin = {},
        onConfirm = {},
        onCancel = {},
    )
}