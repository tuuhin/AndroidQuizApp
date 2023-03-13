package com.eva.firebasequizapp.contribute_quiz.presentation.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.eva.firebasequizapp.contribute_quiz.presentation.CreateQuizViewModel

@Composable
fun CreateQuestions(
    modifier: Modifier = Modifier,
    viewModel: CreateQuizViewModel = hiltViewModel()
) {
    val questions = viewModel.questions
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(questions) { index, item ->
            CreateQuestionCard(index, item)
        }
    }
}