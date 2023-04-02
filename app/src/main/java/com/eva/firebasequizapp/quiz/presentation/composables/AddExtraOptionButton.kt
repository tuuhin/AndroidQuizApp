package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddExtraOptionButton(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable(onClick = onAdd, role = Role.RadioButton),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.weight(0.05f))
        Icon(
            imageVector = Icons.Default.RadioButtonUnchecked,
            contentDescription = "Options Mode",
            tint = Color.Gray,
        )
        Spacer(modifier = Modifier.weight(0.05f))
        Text(
            text = "Add Option",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.85f),
            color = MaterialTheme.colorScheme.primary
        )

    }
}
@Composable
@Preview
private fun AddExtraOptionPreview(){
    AddExtraOptionButton(onAdd = {  })
}