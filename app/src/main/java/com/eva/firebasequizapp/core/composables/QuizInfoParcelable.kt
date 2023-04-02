package com.eva.firebasequizapp.core.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizInfoParcelable(
    quiz: QuizParcelable,
    modifier: Modifier = Modifier,
    showId: Boolean = true,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingValues(bottom = 2.dp))
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            if (showId)
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Question Id: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            append(quiz.uid)
                        }
                    }
                )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = quiz.subject,
                style = MaterialTheme.typography.headlineSmall,
            )
            quiz.desc?.let { desc ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}