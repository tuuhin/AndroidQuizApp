package com.eva.firebasequizapp.quiz.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.presentation.AllQuizzesViewModel
import com.eva.firebasequizapp.quiz.presentation.composables.AllQuizList
import com.eva.firebasequizapp.quiz.util.QuizArrangementStyle
import com.eva.firebasequizapp.quiz.presentation.composables.QuizTabTitleBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllQuizzesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AllQuizzesViewModel = hiltViewModel(),
) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.errorFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.title)
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->

        Column(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .padding(padding)
                .fillMaxSize()
        ) {
            QuizTabTitleBar(
                title = "Your Quizzes",
                arrangementStyle = viewModel.arrangementStyle.value,
                onListStyle = { viewModel.onArrangementChange(QuizArrangementStyle.ListStyle) },
                onGridStyle = { viewModel.onArrangementChange(QuizArrangementStyle.GridStyle) }
            )
            Text(
                text = stringResource(id = R.string.quiz_tab_info),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Note* ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(stringResource(id = R.string.all_quizzes_extra_info))
                }
            }, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val quizContent = viewModel.quizzes.value
                if (quizContent.isLoading)
                    CircularProgressIndicator()
                else if (quizContent.content?.isNotEmpty() == true) {
                    AllQuizList(
                        quizzes = quizContent.content,
                        navController = navController,
                        arrangementStyle = viewModel.arrangementStyle.value,
                        modifier = Modifier.fillMaxSize()
                    )
                } else Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.quiz),
                        contentDescription = "No contribution",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No quizzes are available",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "No quizzes are added or none of them are  approved",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}