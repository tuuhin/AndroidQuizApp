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
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContributionQuizViewModel @Inject constructor(
    private val repo: QuizRepository,
    private val user: FirebaseUser?
) : ViewModel() {

    init { getContributedQuiz() }

    var arrangementStyle = mutableStateOf<QuizArrangementStyle>(QuizArrangementStyle.GridStyle)
        private set

    private val errorMessages = MutableSharedFlow<UiEvent>()
    val errorFlow = errorMessages.asSharedFlow()

    fun onChangeArrangement(event: QuizArrangementStyle) {
        when (event) {
            QuizArrangementStyle.GridStyle -> arrangementStyle.value =
                QuizArrangementStyle.GridStyle
            QuizArrangementStyle.ListStyle -> arrangementStyle.value =
                QuizArrangementStyle.ListStyle
        }
    }

    var contributedQuizzes =
        mutableStateOf<ShowContent<List<QuizModel?>>>(ShowContent(isLoading = true))
        private set

    private fun getContributedQuiz() {
        viewModelScope.launch {
            repo.getCurrentUserContributedQuiz(user!!.uid).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        contributedQuizzes.value = contributedQuizzes.value.copy(
                            isLoading = false,
                            content = null
                        )
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        contributedQuizzes.value = contributedQuizzes.value.copy(
                            isLoading = false,
                            content = res.value
                        )
                    }
                }
            }.launchIn(this)
        }
    }

}