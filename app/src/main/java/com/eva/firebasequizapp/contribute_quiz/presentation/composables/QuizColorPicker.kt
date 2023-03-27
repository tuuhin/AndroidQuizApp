package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.contribute_quiz.util.CreateQuizEvents
import com.eva.firebasequizapp.contribute_quiz.presentation.QuizViewModel


@Composable
fun QuizColorPicker(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val quiz = viewModel.createQuiz.value

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
            R.color.fuchsia_300,
            R.color.pink_300,
            R.color.rose_30
        )
    }
    Column(
        modifier = modifier
            .padding(PaddingValues(top = 4.dp))
    ) {
        Text(text = "Pick Color :", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(2.dp))
        LazyRow(
            modifier = Modifier.height(50.dp)
        ) {
            items(colors.size) { index ->
                val blobColor = colorResource(id = colors[index])
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = colors[index]))
                        .border(
                            2.dp,
                            color = if (quiz.color == blobColor.value)
                                MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                            shape = CircleShape
                        )
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val color = blobColor.value
                            viewModel.onCreateQuizEvent(
                                CreateQuizEvents.OnColorAdded(color)
                            )
                        },
                )
            }
        }
    }
}