package com.eva.firebasequizapp.contribute_quiz.presentation

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.contribute_quiz.data.mappers.toModel
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuizRepository
import com.eva.firebasequizapp.contribute_quiz.domain.use_cases.CreateQuizValidator
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    var user: FirebaseUser?,
    private val repository: CreateQuizRepository
) : ViewModel() {

    private val messages = MutableSharedFlow<UiEvent>()

    val messagesFlow = messages.asSharedFlow()

    private val quizValidator = CreateQuizValidator()

    val createQuiz =
        mutableStateOf(CreateQuizState(createdBy = user?.displayName, creatorUID = user?.uid))

    fun onCreateQuizEvent(events: CreateQuizEvents) {
        when (events) {
            is CreateQuizEvents.ObDescChange -> {
                createQuiz.value = createQuiz.value.copy(
                    desc = events.desc, descError = null
                )
            }
            is CreateQuizEvents.OnImageAdded -> {
                createQuiz.value = createQuiz.value.copy(image = events.uri)
            }
            is CreateQuizEvents.OnSubjectChanges -> {
                createQuiz.value = createQuiz.value.copy(
                    subject = events.subject, subjectError = null
                )
            }
            CreateQuizEvents.OnImageRemoved -> {
                createQuiz.value = createQuiz.value.copy(
                    image = null
                )
            }
            is CreateQuizEvents.OnColorAdded -> {
                createQuiz.value = createQuiz.value.copy(
                    color = events.color
                )
            }
            CreateQuizEvents.OnSubmit -> {
                validate()
            }
        }
    }

    private fun validate() {
        val validateSubject = quizValidator.validateSubject(createQuiz.value)
        val validateDesc = quizValidator.validateDesc(createQuiz.value)
        val errors = listOf(validateSubject, validateDesc).any { !it.isValid }

        if (errors) {
            createQuiz.value = createQuiz.value.copy(
                subjectError = validateSubject.message, descError = validateDesc.message
            )

            return
        } else {
            createQuiz.value = createQuiz.value.copy(
                subjectError = null, descError = null
            )
            addFireBaseQuiz()
        }
    }

    private fun addFireBaseQuiz() {
        viewModelScope.launch(Dispatchers.IO) {

            createQuiz.value.image?.let { image ->
                val uri = repository.uploadQuizImage(image.toString())
                createQuiz.value = createQuiz.value.copy(
                    image = Uri.parse(uri)
                )
            }
            repository.createQuiz(createQuiz.value.toModel())
                .onEach { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            messages.emit(UiEvent.ShowSnackBar(resource.message ?: ""))
                        }

                        is Resource.Success -> {
                            messages.emit(UiEvent.ShowSnackBar("Quiz created "))
                            createQuiz.value = createQuiz.value.copy(
                                subject = "",
                                desc = "",
                                image = null,
                                color = null
                            )
                        }
                        else -> {}
                    }
                }.launchIn(this)

        }
    }

}