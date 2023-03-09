package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class QuizInteractionEvents {
    data class QuizSelected(val quiz: QuizModel) : QuizInteractionEvents()
    object QuizUnselect : QuizInteractionEvents()
}

@HiltViewModel
class AllQuizzesViewModel @Inject constructor(
    private val repo: QuizRepository
) : ViewModel() {

    var showDialog = mutableStateOf(false)
        private set

    var dialogContent = mutableStateOf<QuizModel?>(null)

    var quizzes = mutableStateOf<List<QuizModel?>>(emptyList())
        private set

    init {
        getAllQuizzes()
    }

    private fun getAllQuizzes() {
        viewModelScope.launch {
            repo.getQuiz().onEach { quizzes.value = it }.launchIn(this)
        }
    }

    fun onEvent(event: QuizInteractionEvents) {
        when (event) {
            is QuizInteractionEvents.QuizSelected -> {
                showDialog.value = true
                dialogContent.value = event.quiz
            }
            QuizInteractionEvents.QuizUnselect -> {
                showDialog.value = false
            }
        }
    }


}