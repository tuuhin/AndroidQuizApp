package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonInteractiveQuizCard(
    questionModel: QuestionModel,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    SideEffect {
        Log.d("WORKING",questionModel.toString())
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(.8f)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            if (questionModel.isRequired)
                                append("*")
                            append(questionModel.question)
                        },
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    questionModel.description?.let { desc ->
                        Text(
                            text = desc,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .weight(.2f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete current question",
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            }
            Divider(
                modifier = Modifier.padding(vertical = 2.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(4.dp)
            ) {
                questionModel.options.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "${index + 1}.",
                            modifier = Modifier.weight(.1f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = item,
                            modifier = Modifier.weight(.9f),
                            letterSpacing = 2.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private class NonInteractiveQuizCardParams : PreviewParameterProvider<QuestionModel> {

    override val values = sequenceOf(
        QuestionModel(
            "sdvndsvinvdsnvidsnv",
            question = "What is the the formula of momentum.",
            description = "You may refer the answer to physics book page something",
            isRequired = true,
            options = listOf("p=mv", "p=m/v", "p=m2v"),
            correctAns = "p=mv"
        )
    )
}

@Composable
@Preview
private fun NonInteractiveQuizCardPreview(
    @PreviewParameter(NonInteractiveQuizCardParams::class) questionModel: QuestionModel
) {
    NonInteractiveQuizCard(
        questionModel = questionModel,
        onDelete = {}
    )
}