package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun QuizTabTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    arrangementStyle: QuizArrangementStyle = QuizArrangementStyle.GridStyle,
    onListStyle: () -> Unit,
    onGridStyle: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        when (arrangementStyle) {
            is QuizArrangementStyle.ListStyle -> IconButton(onClick = onGridStyle) {
                Icon(
                    imageVector = Icons.Default.Dns,
                    contentDescription = "List style arrangement"
                )
            }
            is QuizArrangementStyle.GridStyle -> IconButton(onClick = onListStyle) {
                Icon(
                    imageVector = Icons.Default.GridView, contentDescription = "Grid Style"
                )
            }
        }
    }
}