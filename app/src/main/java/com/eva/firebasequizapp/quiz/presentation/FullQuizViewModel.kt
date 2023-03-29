package com.eva.firebasequizapp.quiz.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.core.util.NavParams
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import com.eva.firebasequizapp.quiz.domain.models.CreateQuizResultModel
import com.eva.firebasequizapp.quiz.domain.repository.FullQuizRepository
import com.eva.firebasequizapp.quiz.util.FinalQuizEvent
import com.eva.firebasequizapp.quiz.util.FinalQuizOptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullQuizViewModel @Inject constructor(
    private val repo: FullQuizRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val messages = MutableSharedFlow<UiEvent>()

    var currentQuestions =
        mutableStateOf<ShowContent<List<QuestionModel?>>>(ShowContent(isLoading = true))
        private set

    var attempted = mutableStateOf(0)
        private set
    val optionStates = mutableStateListOf<FinalQuizOptionState>()

    val infoMessages = messages.asSharedFlow()

    private var quizId: String? = null

    init {
        quizId = savedStateHandle.get<String>(NavParams.QUIZ_ID)
        quizId?.let { id -> getQuizQuestion(id) }
    }

    fun onOptionEvent(event: FinalQuizEvent) {
        when (event) {
            is FinalQuizEvent.OptionPicked -> {
                if (optionStates[event.index].option == null) {
                    attempted.value = attempted.value + 1
                }
                optionStates[event.index] = optionStates[event.index].copy(
                    option = event.option,
                    isCorrect = event.option == event.question.correctAns
                )
            }
            is FinalQuizEvent.OptionUnpicked -> {
                if (optionStates[event.index].option != null) {
                    attempted.value = attempted.value - 1
                }
                optionStates[event.index] = optionStates[event.index].copy(
                    option = null,
                    isCorrect = false
                )
            }
            FinalQuizEvent.SubmitQuiz -> {
                onSubmit()
            }
        }
    }

    private fun onSubmit() {
        val count = optionStates.count { it.isCorrect }
        viewModelScope.launch {
            val result = CreateQuizResultModel(
                quizId = quizId!!,
                totalQuestions = optionStates.size,
                correct = count
            )
            repo.setResult(result).onEach { res ->
                Log.d("TAG", res.toString())
            }.launchIn(this)
        }
    }

    private fun getQuizQuestion(uid: String) {
        viewModelScope.launch {
            repo.getAllQuestions(uid).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        currentQuestions.value = currentQuestions.value.copy(
                            isLoading = false, content = null
                        )
                        messages.emit(UiEvent.ShowSnackBar(res.message ?: ""))
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        // New options states are created for the number of question available to the current quiz
                        optionStates.addAll(List(res.value?.size ?: 0) { FinalQuizOptionState() })
                        currentQuestions.value = currentQuestions.value.copy(
                            isLoading = false,
                            content = res.value
                        )
                    }
                }
            }.launchIn(this)
        }
    }


}