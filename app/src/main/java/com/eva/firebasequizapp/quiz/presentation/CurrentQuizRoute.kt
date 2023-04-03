package com.eva.firebasequizapp.quiz.presentation

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.R
import com.eva.firebasequizapp.core.composables.NoContentPlaceHolder
import com.eva.firebasequizapp.core.composables.QuizInfoParcelable
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.data.parcelable.QuizParcelable
import com.eva.firebasequizapp.quiz.data.parcelable.toParcelable
import com.eva.firebasequizapp.quiz.presentation.composables.FinalQuizInfoExtra
import com.eva.firebasequizapp.quiz.presentation.composables.InterActiveQuizCard
import com.eva.firebasequizapp.quiz.util.FinalQuizEvent
import com.eva.firebasequizapp.quiz.util.FullQuizState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentQuizRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    parcelable: QuizParcelable? = null,
    isBackHandlerEnabled: Boolean,
    fullQuizState: FullQuizState,
    viewModel: FullQuizViewModel = hiltViewModel()
) {
    val backMessage = stringResource(id = R.string.back_not_allowed)

    BackHandler(
        enabled = isBackHandlerEnabled,
        onBack = { viewModel.onBackClicked(backMessage) }
    )

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.infoMessages.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                is UiEvent.NavigateBack -> navController.navigateUp()
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Start Quiz") },
                navigationIcon = {
                    if (navController.currentBackStackEntry != null && !isBackHandlerEnabled)
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Button"
                            )
                        }
                },
            )
        },
        floatingActionButton = {
            if (fullQuizState.questions.isNotEmpty() && !fullQuizState.isQuestionLoading) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.onOptionEvent(FinalQuizEvent.SubmitQuiz) }
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Submitting")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "Submit")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            parcelable?.let { QuizInfoParcelable(quiz = it) }
            if (fullQuizState.isLoading)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() }
                )
            else if (fullQuizState.quiz != null)
                QuizInfoParcelable(
                    quiz = fullQuizState.quiz.toParcelable()
                )
            else if (fullQuizState.isQuestionLoading)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() }
                )
            else if (fullQuizState.questions.isEmpty())
                NoContentPlaceHolder(
                    primaryText = "Nothing Found",
                    imageRes = R.drawable.confused,
                    secondaryText = "Seems it's mistakenly got approved.Quiz with zero questions aren't possible.Sorry for the mistake",
                    graphicsLayer = {
                        rotationX = 12.5f
                    }
                )
            else {
                FinalQuizInfoExtra(
                    attempted = viewModel.quizState.value.attemptedCount,
                    content = fullQuizState.questions,
                )
                Divider(
                    modifier = Modifier
                        .padding(PaddingValues(top = 4.dp))
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                LazyColumn(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    itemsIndexed(fullQuizState.questions) { idx, item ->
                        item?.let {
                            InterActiveQuizCard(optionState = viewModel.quizState.value.optionsState,
                                quiz = item,
                                quizIndex = idx,
                                onUnpick = {
                                    viewModel.onOptionEvent(FinalQuizEvent.OptionUnpicked(idx))
                                },
                                onPick = { option ->
                                    viewModel.onOptionEvent(
                                        FinalQuizEvent.OptionPicked(idx, option, item)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}