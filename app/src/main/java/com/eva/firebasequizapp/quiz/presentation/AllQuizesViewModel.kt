package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import com.eva.firebasequizapp.quiz.presentation.composables.QuizArrangementStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
                dialogContent.value = event.quiz
            }
            QuizInteractionEvents.QuizUnselect -> {
                showDialog.value = false
            }
        }
    }


}