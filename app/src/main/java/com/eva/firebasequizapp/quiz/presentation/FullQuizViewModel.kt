package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.CreateQuizResultModel
import com.eva.firebasequizapp.quiz.domain.repository.FullQuizRepository
import com.eva.firebasequizapp.quiz.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullQuizViewModel @Inject constructor(
    private val repo: FullQuizRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var fullQuizState = mutableStateOf(FullQuizState())
        private set

    var routeState = mutableStateOf(FinalQuizRouteState())
        private set

    var quizState = mutableStateOf(FinalQuizState())
        private set

    private val messages = MutableSharedFlow<UiEvent>()
    val infoMessages = messages.asSharedFlow()

    private var quizId: String? = null

    init {
        quizId = savedStateHandle.get<String>(NavParams.QUIZ_ID)
        val valid = savedStateHandle.get<Boolean>(NavParams.SOURCE_VALID_ID)
        fullQuizState.value = fullQuizState.value.copy(isQuizPresent = valid ?: false)
        quizId?.let { id -> getQuizQuestion(id, valid) }
    }

    fun onBackClicked() {
        val message =
            "Complete the Quiz First,cannot pop out of the quiz screen,otherwise cancel the running quiz"
        viewModelScope.launch { messages.emit(UiEvent.ShowSnackBar(message)) }
    }

    fun onOptionEvent(event: FinalQuizEvent) {
        when (event) {
            is FinalQuizEvent.OptionPicked -> {
                if (quizState.value.optionsState[event.index].option == null) {
                    quizState.value = quizState.value.copy(
                        attemptedCount = quizState.value.attemptedCount + 1
                    )
                }
                quizState.value.optionsState[event.index] =
                    quizState.value.optionsState[event.index].copy(
                        option = event.option,
                        isCorrect = event.option == event.question.correctAns
                    )
            }
            is FinalQuizEvent.OptionUnpicked -> {
                if (quizState.value.optionsState[event.index].option != null) {
                    quizState.value = quizState.value.copy(
                        attemptedCount = quizState.value.attemptedCount - 1
                    )
                }
                quizState.value.optionsState[event.index] =
                    quizState.value.optionsState[event.index].copy(
                        option = null,
                        isCorrect = false
                    )
            }
            FinalQuizEvent.SubmitQuiz -> {
                routeState.value = routeState.value.copy(
                    showDialog = true,
                    isBackNotAllowed = false
                )
            }
        }
    }

    fun onDialogEvent(event: FinalQuizDialogEvents) {
        when (event) {
            FinalQuizDialogEvents.ContinueQuiz -> {
                routeState.value = routeState.value.copy(
                    showDialog = false,
                    isBackNotAllowed = false
                )
            }
            FinalQuizDialogEvents.SubmitQuiz -> {
                routeState.value = routeState.value.copy(
                    showDialog = false,
                    isBackNotAllowed = true
                )
                onSubmit()
            }
        }
    }


    private fun onSubmit() {
        val count = quizState.value.optionsState.count { it.isCorrect }
        viewModelScope.launch {
            if (quizState.value.optionsState.isEmpty()) {
                messages.emit(UiEvent.ShowSnackBar("Cannot add result for this quiz as there are no questions found"))
                return@launch
            }
            val result = CreateQuizResultModel(
                quizId = quizId!!,
                totalQuestions = quizState.value.optionsState.size,
                correct = count
            )
            repo.setResult(result).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        messages.emit(UiEvent.ShowSnackBar(res.message ?: ""))
                    }
                    is Resource.Loading -> {
                        messages.emit(UiEvent.ShowSnackBar("Submitting your quiz"))
                    }
                    is Resource.Success -> {
                        messages.emit(UiEvent.NavigateBack)
                    }
                }
            }.launchIn(this)
        }
    }

    private fun getQuizQuestion(uid: String, valid: Boolean?) {
        viewModelScope.launch {
            if (valid == false)
                when (val quiz = repo.getCurrentQuiz(uid)) {
                    is Resource.Error -> {
                        fullQuizState.value = fullQuizState.value.copy(
                            isLoading = false, quiz = null
                        )
                        messages.emit(UiEvent.ShowSnackBar(message = quiz.message ?: ""))
                        return@launch
                    }
                    is Resource.Success -> {
                        fullQuizState.value = fullQuizState.value.copy(
                            isLoading = false, quiz = quiz.value
                        )
                    }
                    else -> {}
                }
            fullQuizState.value = fullQuizState.value.copy(
                isLoading = false, quiz = null
            )
            repo.getAllQuestions(uid).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        fullQuizState.value = fullQuizState.value
                            .copy(
                                isQuestionLoading = false,
                                questions = emptyList()
                            )
                        messages.emit(UiEvent.ShowSnackBar(message = res.message ?: ""))
                    }
                    is Resource.Success -> {
                        quizState.value.optionsState.addAll(
                            List(size = res.value?.size ?: 0) { FinalQuizOptionState() }
                        )
                        fullQuizState.value = fullQuizState.value
                            .copy(
                                isQuestionLoading = false,
                                questions = res.value!!
                            )
                    }
                    is Resource.Loading -> {
                        fullQuizState.value = fullQuizState.value
                            .copy(isQuestionLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }


}