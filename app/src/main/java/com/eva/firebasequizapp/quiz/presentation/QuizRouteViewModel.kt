package com.eva.firebasequizapp.quiz.presentation

import androidx.lifecycle.ViewModel
import com.eva.firebasequizapp.auth.data.UserAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizRouteViewModel @Inject constructor(
    private val repository: UserAuthRepository
) : ViewModel() {
    fun logout() = repository.logout()
}