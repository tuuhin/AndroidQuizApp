package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
                text = "Give Quiz",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.home_tab_card_info),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = quizId,
                    label = { Text(text = "Uid")},
                    placeholder = { Text(text = "67dHwZtSWHS2cMV57zdF")},
                    onValueChange = { quizId = it },
                    modifier = Modifier.fillMaxWidth(.72f),
                    shape = RoundedCornerShape(10.dp)
                )
                Button(onClick = {}) {
                    Text(text = "Join")
                }
            }
        }
    }
}

@Composable
@Preview
private fun JoinQuizCardPreview(){
    JoinQuizCard()
}