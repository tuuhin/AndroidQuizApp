package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import com.eva.firebasequizapp.quiz.util.QuizArrangementStyle
import com.eva.firebasequizapp.quiz.util.QuizInteractionEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllQuizzesViewModel @Inject constructor(
    private val repo: QuizRepository
) : ViewModel() {

    var showDialog = mutableStateOf(false)
        private set

    var selectedQuiz = mutableStateOf<QuizModel?>(null)
        private set

    var quizzes = mutableStateOf<ShowContent<List<QuizModel?>>>(ShowContent(isLoading = true))
        private set

    private val errorMessages = MutableSharedFlow<UiEvent>()
    val errorFlow = errorMessages.asSharedFlow()

    var arrangementStyle = mutableStateOf<QuizArrangementStyle>(QuizArrangementStyle.GridStyle)
        private set

    init {
        getAllQuizzes()
    }

    fun onArrangementChange(event: QuizArrangementStyle) {
        when (event) {
            QuizArrangementStyle.GridStyle -> {
                arrangementStyle.value = event
            }
            QuizArrangementStyle.ListStyle -> {
                arrangementStyle.value = event
            }
        }
    }

    private fun getAllQuizzes() {
        viewModelScope.launch {
            repo.getAllQuizzes().onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        errorMessages.emit(UiEvent.ShowSnackBar(res.message ?: ""))
                        quizzes.value = quizzes.value.copy(isLoading = false, content = null)
                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        quizzes.value = quizzes.value.copy(content = res.value, isLoading = false)
                    }
                }
            }.launchIn(this)
        }
    }

    fun onEvent(event: QuizInteractionEvents) {
        when (event) {
            is QuizInteractionEvents.QuizSelected -> {
                showDialog.value = true
                selectedQuiz.value = event.quiz
            }
            QuizInteractionEvents.QuizUnselect -> {
                showDialog.value = false
            }
        }
    }
}