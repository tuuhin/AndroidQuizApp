package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinQuizCard(
    modifier: Modifier = Modifier
) {
    var quizId by remember { mutableStateOf("") }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Enter Quiz Code",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.home_tab_card_info),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = quizId,
                    label = { Text(text = "Uid") },
                    placeholder = { Text(text = "Ex: 67dHw.....") },
                    onValueChange = { quizId = it },
                    modifier = Modifier.fillMaxWidth(.72f),
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = {}) {
                    Text(text = "Join")
                }
            }
        }
    }
}

@Composable
@Preview
private fun JoinQuizCardPreview() {
    JoinQuizCard()
}