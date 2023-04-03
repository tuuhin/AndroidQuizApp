package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizResultsRepository
import com.eva.firebasequizapp.quiz.domain.use_case.DocumentIdValidator
import com.eva.firebasequizapp.quiz.util.DeleteQuizResultsEvent
import com.eva.firebasequizapp.quiz.util.DeleteQuizResultsState
import com.eva.firebasequizapp.quiz.util.SearchQuizEvents
import com.eva.firebasequizapp.quiz.util.SearchQuizState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: QuizResultsRepository,
) : ViewModel() {

    private val idValidator = DocumentIdValidator()

    var content = mutableStateOf<ShowContent<List<QuizResultModel?>>>(ShowContent(isLoading = true))
        private set

    private val messages = MutableSharedFlow<UiEvent>()

    var deleteQuizState = mutableStateOf(DeleteQuizResultsState())
        private set

    val uiEvent = messages.asSharedFlow()

    var searchQuizState = mutableStateOf(SearchQuizState())
        private set

    init {
        getResults()
    }

    fun onQuizSearch(event: SearchQuizEvents) {
        when (event) {
            is SearchQuizEvents.OnQuizIdChanged -> {
                searchQuizState.value =
                    searchQuizState.value.copy(quizId = event.id)
            }
            SearchQuizEvents.Search -> {
                val validator = idValidator.execute(searchQuizState.value.quizId)
                searchQuizState.value = searchQuizState.value.copy(
                    quizIdError = if (!validator.isValid)
                        validator.message
                    else null
                )
                if (searchQuizState.value.quizIdError == null)
                    searchQuizState.value = searchQuizState.value.copy(
                        showDialog = true
                    )
            }
            SearchQuizEvents.OnSearchCancelled -> searchQuizState.value =
                searchQuizState.value.copy(showDialog = false)
            SearchQuizEvents.OnSearchConfirmed -> {
                searchQuizState.value =
                    searchQuizState.value.copy(showDialog = false, isConfirmed = true)
                viewModelScope.launch {
                    messages.emit(UiEvent.NavigateBack)
                }
            }
        }
    }

    fun onDeleteResult(event: DeleteQuizResultsEvent) {
        when (event) {
            is DeleteQuizResultsEvent.ResultsSelected -> {
                deleteQuizState.value = deleteQuizState.value.copy(
                    isDialogOpen = true, result = event.result
                )
            }
            DeleteQuizResultsEvent.DeleteCanceled -> {
                deleteQuizState.value =
                    deleteQuizState.value.copy(isDialogOpen = false, result = null)
            }
            DeleteQuizResultsEvent.DeleteConfirmed -> {
                deleteQuizResults()
                deleteQuizState.value =
                    deleteQuizState.value.copy(isDialogOpen = false, result = null)
            }
        }
    }

    private fun getResults() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuizResults().onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        content.value = content.value.copy(isLoading = false, content = null)
                        messages.emit(UiEvent.ShowSnackBar(res.message ?: ""))
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        content.value = content.value.copy(
                            isLoading = false,
                            content = res.value
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private fun deleteQuizResults() {
        val result = deleteQuizState.value.result
        if (result != null)
            viewModelScope.launch(Dispatchers.IO) {
                when (val resp = repository.deleteQuizResults(result.uid)) {
                    is Resource.Error -> {
                        messages.emit(UiEvent.ShowSnackBar(resp.message ?: ""))
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        messages.emit(UiEvent.ShowSnackBar("Removed Result for ${result.quiz?.subject ?: "null"}"))
                    }
                }
            }
    }


}