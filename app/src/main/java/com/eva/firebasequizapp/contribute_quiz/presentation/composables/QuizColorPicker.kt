package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.R

@Composable
fun QuizColorPicker(
    modifier: Modifier = Modifier
) {
    val colors = remember {
        listOf(
            R.color.red_300,
            R.color.orange_300,
            R.color.amber_300,
            R.color.yellow_300,
            R.color.lime_300,
            R.color.green_300,
            R.color.emerald_300,
            R.color.teal_200,
            R.color.cyan_300,
            R.color.sky_300,
            R.color.blue_300,
            R.color.indigo_300,
            R.color.violet_300,
            R.color.pink_300,
            R.color.fuchsia_300,
            R.color.pink_300,
            R.color.rose_30
        )
    }
    Column(
        modifier = modifier
            .padding(0.dp, 4.dp)
            .height(50.dp)
    ) {
        Text(text = "Pick Color :", color = MaterialTheme.colorScheme.secondary)
        LazyRow {
            items(colors.size) { index ->
                Row {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = colors[index]))
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = stringResource(id = R.string.create_quiz_info),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.secondary,
        fontStyle = FontStyle.Italic,
        modifier = Modifier.padding(2.dp)
    )
}