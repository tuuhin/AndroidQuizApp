package com.eva.firebasequizapp.quiz.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultsCard(
    result: QuizResultModel,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.padding(2.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            result.quiz?.let { model ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text(text = model.subject, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(2.dp))
                        model.desc?.let { desc ->
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(horizontal = 2.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color(android.graphics.Color.parseColor(model.color)))

                    )
                }
            }
            LinearProgressIndicator(
                progress = result.correct.toFloat() / result.totalQuestions,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(MaterialTheme.shapes.small),
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Total: ${result.totalQuestions}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Correct: ${result.correct} ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
