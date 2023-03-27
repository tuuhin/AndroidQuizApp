package com.eva.firebasequizapp.quiz.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.core.util.NavRoutes
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.presentation.ContributionQuizViewModel
import com.eva.firebasequizapp.quiz.presentation.composables.ContributedQuizList
import com.eva.firebasequizapp.quiz.util.QuizArrangementStyle
import com.eva.firebasequizapp.quiz.presentation.composables.QuizTabTitleBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizContributionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ContributionQuizViewModel = hiltViewModel()
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
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(NavRoutes.NavCreateQuizRoute.route) }
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Create another quiz")
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Create")
            }
        }) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            QuizTabTitleBar(
                title = "Your Contribution",
                arrangementStyle = viewModel.arrangementStyle.value,
                onListStyle = { viewModel.onChangeArrangement(QuizArrangementStyle.ListStyle) },
                onGridStyle = { viewModel.onChangeArrangement(QuizArrangementStyle.GridStyle) }
            )
            Text(
                text = stringResource(id = R.string.contribute_quiz_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(PaddingValues(bottom = 2.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val quizContent = viewModel.contributedQuizzes.value
                if (quizContent.isLoading)
                    CircularProgressIndicator()
                else if (quizContent.content?.isNotEmpty() == true) {
                    ContributedQuizList(
                        quizzes = quizContent.content,
                        navController = navController,
                        style = viewModel.arrangementStyle.value,
                        modifier = Modifier.fillMaxSize()
                    )
                } else Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jigsaw),
                        contentDescription = "No contribution",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = 12.5f
                            }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No contributions are made",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Contributions really helps the content to grow.",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}