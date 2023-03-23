package com.eva.firebasequizapp.contribute_quiz.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.contribute_quiz.domain.repository.QuestionsRepository
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.ShowContent
import com.eva.firebasequizapp.core.util.UiEvent
import com.eva.firebasequizapp.quiz.domain.models.QuestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val repo: QuestionsRepository
) : ViewModel() {

    private val messages = MutableSharedFlow<UiEvent>()
    val errorMessages = messages.asSharedFlow()

    val questions = mutableStateOf<ShowContent<List<QuestionModel?>>>(
        ShowContent(
            isLoading = true,
            content = null
        )
    )

    fun getCurrentQuizQuestions(uid: String) {
        viewModelScope.launch {
            repo.getQuestions(uid).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        questions.value = questions.value.copy(isLoading = false, content = null)
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Log.d("TAGS", res.value.toString())
                        questions.value = questions.value.copy(
                            content = res.value,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun deleteQuestion(questionModel: QuestionModel) {
        val exists = questions.value.content?.find { it == questionModel }
        if (exists != null) {
            viewModelScope.launch {
            }
        }
    }
}