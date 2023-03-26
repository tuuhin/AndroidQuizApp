package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.contribute_quiz.domain.repository.QuestionsRepository
import com.eva.firebasequizapp.contribute_quiz.util.DeleteDialogState
import com.eva.firebasequizapp.contribute_quiz.util.QuestionDeleteEvent
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val repo: QuestionsRepository
) : ViewModel() {

    private val messages = MutableSharedFlow<UiEvent>()
    val errorMessages = messages.asSharedFlow()

    var deleteDialogState = mutableStateOf(DeleteDialogState())
        private set

    val questions = mutableStateOf<ShowContent<List<QuestionModel?>>>(
        ShowContent(
            isLoading = true,
            content = null
        )
    )

    fun onQuestionDelete(event: QuestionDeleteEvent) {
        when (event) {
            QuestionDeleteEvent.DeleteConfirmed -> {
                deleteDialogState.value.model?.let { model ->
                    deleteQuestion(model)
                }
                deleteDialogState.value = deleteDialogState.value.copy(
                    isDialogOpen = false,
                    model = null
                )
            }
            is QuestionDeleteEvent.QuestionSelected -> {
                deleteDialogState.value = deleteDialogState.value.copy(
                    isDialogOpen = true,
                    model = event.model
                )
            }
            QuestionDeleteEvent.CloseDeleteDialog ->{
                deleteDialogState.value = deleteDialogState.value.copy(
                    isDialogOpen = false,
                    model = null
                )
            }
        }
    }

    fun getCurrentQuizQuestions(uid: String) {
        viewModelScope.launch {
            repo.getQuestions(uid).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        questions.value = questions.value.copy(isLoading = false, content = null)
                    }
                    is Resource.Success -> {
                        questions.value = questions.value.copy(
                            content = res.value,
                            isLoading = false
                        )
                    }
                    else -> {}
                }
            }.launchIn(this)
        }
    }

    private fun deleteQuestion(questionModel: QuestionModel) {
        val model = questions.value.content?.find { it == questionModel }
        if (model != null) {
            viewModelScope.launch {
                repo.deleteQuestion(model)
                    .onEach { res ->
                        when (res) {
                            is Resource.Error -> {
                                messages.emit(
                                    UiEvent.ShowSnackBar(
                                        res.message ?: "Cannot remove the question"
                                    )
                                )
                            }
                            is Resource.Success -> {
                                messages.emit(UiEvent.ShowSnackBar("Question ${model.question} has been removed "))
                            }
                            else -> {}
                        }
                    }.launchIn(this)
            }
        }
    }
}