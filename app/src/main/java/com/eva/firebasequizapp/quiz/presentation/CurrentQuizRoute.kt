package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.core.composables.QuizInfoParcelable
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable
import com.eva.firebasequizapp.quiz.presentation.composables.InterActiveQuizCard
import com.eva.firebasequizapp.quiz.util.FinalQuizEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentQuizRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    parcelable: QuizParcelable? = null,
    viewModel: FullQuizViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.infoMessages.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.title)
                else -> {}
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }, topBar = {
        SmallTopAppBar(title = { Text(text = "Start Quiz") }, navigationIcon = {
            if (navController.currentBackStackEntry != null) IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button"
                )
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { viewModel.onOptionEvent(FinalQuizEvent.SubmitQuiz) }) {
            Icon(imageVector = Icons.Default.Done, contentDescription = "Submitting")
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "Submit")
        }
    }) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            val content = viewModel.currentQuestions.value
            if (content.isLoading) Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            else if (content.content?.isNotEmpty() == true) {
                val attempted = viewModel.attempted.value
                parcelable?.let { quiz ->
                    QuizInfoParcelable(quiz = quiz)
                }
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Slider(
                            value = attempted.toFloat() / content.content.size,
                            onValueChange = {},
                            steps = content.content.size,
                            colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                inactiveTickColor = MaterialTheme.colorScheme.surfaceTint,
                                activeTickColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Your Progress",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "${attempted}/${content.content.size}",
                                letterSpacing = 2.sp,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier
                        .padding(PaddingValues(top = 4.dp))
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                LazyColumn {
                    itemsIndexed(content.content) { idx, item ->
                        item?.let {
                            InterActiveQuizCard(quiz = item, quizIndex = idx)
                        }
                    }
                }
            } else Text(text = "No questions found")
        }
    }

}