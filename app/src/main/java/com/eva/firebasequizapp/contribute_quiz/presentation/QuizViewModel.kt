package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    var user: FirebaseUser?
) : ViewModel() {

    val createQuiz = mutableStateOf(CreateQuizState(createdBy = user?.displayName))

    fun onCreateQuizEvent(events: CreateQuizEvents) {
        when (events) {
            is CreateQuizEvents.ObDescChange -> {
                createQuiz.value = createQuiz.value.copy(desc = events.desc)
            }
            is CreateQuizEvents.OnImageAdded -> {
                createQuiz.value = createQuiz.value.copy(image = events.uri)
            }
            is CreateQuizEvents.OnSubjectChanges -> {
                createQuiz.value = createQuiz.value.copy(
                    subject = events.subject
                )
            }
        }
    }

}