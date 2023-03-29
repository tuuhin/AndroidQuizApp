package com.eva.firebasequizapp.quiz.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.QuizResultModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    var content = mutableStateOf<ShowContent<List<QuizResultModel?>>>(ShowContent(isLoading = true))
        private set

    private val messages = MutableSharedFlow<UiEvent>()

    val uiEvent = messages.asSharedFlow()

    init {
        getResults()
    }

    private fun getResults() {
        viewModelScope.launch {
            repository.getQuizResults().onEach { res ->
                Log.d("TAG",res.toString())
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


}