package com.eva.firebasequizapp.core.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.R

@Composable
@Preview
fun NoQuestionsFound(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(.75f),
    ) {
        Image(
            painter = painterResource(id = R.drawable.confused),
            contentDescription = "No questions found",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.surfaceTint
            ),
            modifier = Modifier
                .scale(.75f)
                .padding(2.dp)
        )
        Text(
            text = "No questions are added the quiz is blank",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Add some questions to make it functional",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}