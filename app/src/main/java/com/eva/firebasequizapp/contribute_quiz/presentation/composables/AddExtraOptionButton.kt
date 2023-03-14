package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AddExtraOptionButton(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(0.05f))
        Icon(
            imageVector = Icons.Default.RadioButtonUnchecked,
            contentDescription = "Options Mode",
            tint = Color.Gray,

        )
        Spacer(modifier = Modifier.weight(0.05f))
        TextButton(
            onClick = onAdd
        ) {
            Text(
                text = "Add Option",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(0.85f)
            )

        }
    }
}