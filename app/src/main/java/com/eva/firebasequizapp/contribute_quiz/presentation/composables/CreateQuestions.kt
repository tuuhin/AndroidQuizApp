package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionEvent
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuestionViewModel
import com.eva.firebasequizapp.core.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestions(
    uid: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestionViewModel = hiltViewModel()
) {
    val questions = viewModel.questions

    val snackBarState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.messages.collectLatest { event ->
            when (event) {
                is UiEvent.ShowDialog -> {}
                is UiEvent.ShowSnackBar -> {
                    snackBarState.showSnackbar(event.title)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        topBar = {
        SmallTopAppBar(title = { Text(text = uid) }, navigationIcon = {
            if (navController.previousBackStackEntry != null) IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow Back"
                )
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            viewModel.onQuestionEvent(
                CreateQuestionEvent.SubmitQuestions(
                    uid
                )
            )
        }) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Save the questions")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Save")
        }
    }) { padding ->
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            itemsIndexed(questions) { index, item ->
                CreateQuestionCard(index, item)
            }
            item {
                OutlinedButton(
                    onClick = { viewModel.onQuestionEvent(CreateQuestionEvent.QuestionAdded) },
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(2.dp, 0.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Questions Add")
                    Text(text = "Add Question")
                }
            }
        }
    }
}