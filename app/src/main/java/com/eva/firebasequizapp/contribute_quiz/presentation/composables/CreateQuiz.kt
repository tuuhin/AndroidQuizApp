package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuiz(
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(.8f)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Untitled",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                TextField(
                    value = desc,
                    onValueChange = { desc = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Some description",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium
                )
            }

            Box(
                modifier = Modifier
                    .weight(.2f)
                    .padding(2.dp)
                    .fillMaxHeight()
                    .border(BorderStroke(2.dp, Color.Gray)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.BrokenImage, contentDescription = "Image")
            }
        }
    }
}