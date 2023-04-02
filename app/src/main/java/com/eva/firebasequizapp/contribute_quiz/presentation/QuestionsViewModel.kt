package com.eva.firebasequizapp.contribute_quiz.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.contribute_quiz.domain.repository.QuestionsRepository
import com.eva.firebasequizapp.contribute_quiz.util.*
import com.eva.firebasequizapp.core.firebase_paths.FireStoreCollections
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
    private val repo: QuestionsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    init {
        val data = savedStateHandle.get<String>(FireStoreCollections.QUID_ID_FIELD)
        if (data != null) getCurrentQuizQuestions(data)
    }

    private val messages = MutableSharedFlow<UiEvent>()
    val errorMessages = messages.asSharedFlow()

    var deleteQuestionState = mutableStateOf(DeleteQuestionsState())
        private set

    var deleteQuizState = mutableStateOf(DeleteWholeQuizState())
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
                deleteQuestionState.value.model?.let { model ->
                    deleteQuestion(model)
                }
                deleteQuestionState.value = deleteQuestionState.value.copy(
                    isDialogOpen = false,
                    model = null
                )
            }
            is QuestionDeleteEvent.QuestionSelected -> {
                deleteQuestionState.value = deleteQuestionState.value.copy(
                    isDialogOpen = true,
                    model = event.model
                )
            }
            QuestionDeleteEvent.CloseDeleteDialog -> {
                deleteQuestionState.value = deleteQuestionState.value.copy(
                    isDialogOpen = false,
                    model = null
                )
            }
        }
    }


    fun onDeleteQuizEvent(event: DeleteQuizEvents) {
        when (event) {
            DeleteQuizEvents.OnDeleteCanceled -> {
                deleteQuizState.value = deleteQuizState.value.copy(
                    showDialog = false,
                    quizId = null
                )
            }
            DeleteQuizEvents.OnDeleteConfirmed -> {
                deleteQuizState.value = deleteQuizState.value.copy(isDeleting = true)
                //actual delete
                deleteQuizState.value.quizId?.let { deleteQuiz(it) }
            }
            is DeleteQuizEvents.PickQuiz -> {
                deleteQuizState.value = deleteQuizState.value.copy(
                    showDialog = true,
                    quizId = event.quizId
                )
            }
        }
    }

    private fun deleteQuiz(id: String) {
        Log.d("UID", id)
        // then close the dialog
        viewModelScope.launch {
            repo.deleteQuiz(id).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        deleteQuizState.value = deleteQuizState.value.copy(
                            isDeleting = false,
                            showDialog = false,
                            quizId = null
                        )
                        messages.emit(UiEvent.ShowSnackBar(res.message ?: ""))
                    }
                    is Resource.Loading -> {
                        deleteQuizState.value = deleteQuizState.value.copy(
                            isDeleting = true,
                        )
                    }
                    is Resource.Success -> {
                        deleteQuizState.value = deleteQuizState.value.copy(
                            isDeleting = false,
                            showDialog = false,
                            quizId = null
                        )
                        messages.emit(UiEvent.NavigateBack)
                    }
                }

            }.launchIn(this)
        }
    }

    private fun getCurrentQuizQuestions(uid: String) {
        viewModelScope.launch {
            repo.getQuestions(uid).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        questions.value = questions.value.copy(isLoading = false, content = null)
                        messages.emit(UiEvent.ShowSnackBar(res.message ?: ""))
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