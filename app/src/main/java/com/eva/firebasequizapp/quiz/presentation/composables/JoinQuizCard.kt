package com.eva.firebasequizapp.quiz.presentation.composables


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.JoinFull
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

    Card(
        modifier = modifier,
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
                value = quizId,
                label = { Text(text = "Uid") },
                placeholder = { Text(text = "Ex: 67dHw.....") },
                onValueChange = { quizId = it },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Button(
                onClick = {},
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(imageVector = Icons.Outlined.JoinFull, contentDescription = "Join the quiz")
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "Join")
            }
        }
    }
}

@Composable
@Preview
private fun JoinQuizCardPreview() {
    JoinQuizCard()
}