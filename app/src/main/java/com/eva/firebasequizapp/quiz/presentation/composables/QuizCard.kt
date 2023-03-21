package com.eva.firebasequizapp.quiz.presentation.composables

import android.content.Context
import android.graphics.Color as Parser
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizCard(
    quiz: QuizModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    arrangement: QuizArrangementStyle = QuizArrangementStyle.GridStyle,
    context: Context = LocalContext.current
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(Parser.parseColor(quiz.color))
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            when (arrangement) {
                QuizArrangementStyle.GridStyle -> {
                    if (quiz.image != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(quiz.image).build(),
                            contentDescription = "User photo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = quiz.subject,
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    quiz.desc?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium
                                .copy(fontWeight = FontWeight.Normal),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3,
                            modifier = Modifier.padding(0.dp, 2.dp)
                        )
                    }
                }
                QuizArrangementStyle.ListStyle -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(.75f)
                        ) {
                            Text(
                                text = quiz.subject,
                                style = MaterialTheme.typography.titleMedium
                                    .copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            quiz.desc?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.labelMedium
                                        .copy(fontWeight = FontWeight.Normal),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 3,
                                    modifier = Modifier.padding(0.dp, 2.dp)
                                )
                            }
                        }
                        if (quiz.image != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(quiz.image).build(),
                                contentDescription = "User photo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .weight(0.25f)
                            )
                        }
                    }
                }
            }
            Divider(
                color = MaterialTheme.colorScheme.secondary,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = quiz.lastUpdate.format(DateTimeFormatter.ISO_DATE),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
