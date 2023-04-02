package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StarProgressIndicator(
    progress: Float, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 2.dp)
    ) {

        val filledStarCount = (progress * 5).toInt()
        val unfilledStarCount = 5 - filledStarCount

        repeat(filledStarCount) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.padding(horizontal = 1.dp)
            )
        }
        repeat(unfilledStarCount) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.padding(horizontal = 1.dp)
            )
        }
    }
}