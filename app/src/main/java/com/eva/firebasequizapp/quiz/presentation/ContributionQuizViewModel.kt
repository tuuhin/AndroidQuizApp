package com.eva.firebasequizapp.quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.quiz.domain.models.QuizModel
import com.eva.firebasequizapp.quiz.domain.repository.QuizRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QuizArrangementStyle {
    object GridStyle : QuizArrangementStyle()
    object ListStyle : QuizArrangementStyle()
}

@HiltViewModel
class ContributionQuizViewModel @Inject constructor(
    private val repo: QuizRepository,
    private val user: FirebaseUser?
) : ViewModel() {


    init {
        getContributedQuiz()
    }

    var arrangementStyle = mutableStateOf<QuizArrangementStyle>(QuizArrangementStyle.GridStyle)
        private set

    fun onChangeArrangement(event: QuizArrangementStyle) {
        when (event) {
            QuizArrangementStyle.GridStyle -> arrangementStyle.value =
                QuizArrangementStyle.GridStyle
            QuizArrangementStyle.ListStyle -> arrangementStyle.value =
                QuizArrangementStyle.ListStyle
        }
    }

    var contributedQuizzes = mutableStateOf<List<QuizModel?>>(emptyList())
        private set

    private fun getContributedQuiz() {
        viewModelScope.launch {
            repo.getContributedQuiz(user!!.uid).onEach {
                contributedQuizzes.value = it
            }.launchIn(this)
        }
    }

}