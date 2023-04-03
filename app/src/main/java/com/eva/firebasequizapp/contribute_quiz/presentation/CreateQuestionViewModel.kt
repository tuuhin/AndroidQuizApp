package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.contribute_quiz.data.mappers.toModel
import com.eva.firebasequizapp.contribute_quiz.domain.repository.CreateQuestionsRepo
import com.eva.firebasequizapp.contribute_quiz.domain.use_cases.CreateQuestionValidator
import com.eva.firebasequizapp.contribute_quiz.util.*
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    private val repo: CreateQuestionsRepo, val user: FirebaseUser?
) : ViewModel() {

    private val validator = CreateQuestionValidator()

    var questions = mutableStateListOf(CreateQuestionState())
        private set

    private val messages = MutableSharedFlow<UiEvent>()

    val uiEvents = messages.asSharedFlow()

    var showDialog = mutableStateOf(false)
        private set

    private fun onOptionsEvent(event: OptionsEvent, index: Int) {
        val currentQuestion = questions[index]
        when (event) {
            OptionsEvent.OptionAdded -> currentQuestion.options.add(QuestionOptionsState())
            is OptionsEvent.OptionRemove -> {
                if (currentQuestion.options.size != 1) {
                    currentQuestion.options.remove(event.option)
                    val isRemovedQuestionIsAns = currentQuestion.ansKey == event.option
                    if (isRemovedQuestionIsAns)
                        questions[index] = currentQuestion.copy(ansKey = null)
                    return
                }
                viewModelScope.launch {
                    messages.emit(
                        UiEvent.ShowSnackBar(
                            message = "Cannot create a question without any options"
                        )
                    )
                }
            }
            is OptionsEvent.OptionValueChanged -> {
                currentQuestion.options[event.index] = currentQuestion.options[event.index].copy(
                    option = event.value, optionError = null
                )
            }
        }
    }

    fun onQuestionEvent(event: CreateQuestionEvent) {
        when (event) {
            is CreateQuestionEvent.DescriptionAdded -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(desc = event.desc)
            }
            is CreateQuestionEvent.OnOptionEvent -> {
                val index = questions.indexOf(event.question)
                onOptionsEvent(event.optionEvent, index)
            }
            CreateQuestionEvent.QuestionAdded -> {
                questions.add(CreateQuestionState())
            }
            is CreateQuestionEvent.QuestionQuestionAdded -> {
                val index = questions.indexOf(event.question)
                if (index != -1)
                    questions[index] =
                        questions[index].copy(
                            question = event.value,
                            questionError = null,
                            isDeleteAllowed = event.value.isEmpty()
                        )
            }
            is CreateQuestionEvent.QuestionRemoved -> {
                if (questions.size != 1) {
                    questions.remove(event.question)
                    return
                }
                viewModelScope.launch {
                    messages.emit(
                        UiEvent.ShowSnackBar(
                            message = "There should be at least one question to be added"
                        )
                    )
                    cancel()
                }
            }
            is CreateQuestionEvent.ToggleQuestionDesc -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(
                    desc = if (questions[index].desc == null) "" else null
                )
            }
            is CreateQuestionEvent.ToggleRequiredField -> {
                val index = questions.indexOf(event.question)
                val isRequired = questions[index].required
                questions[index] = questions[index].copy(required = !isRequired)
            }
            is CreateQuestionEvent.SetEditableMode -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(state = QuestionsViewMode.Editable)
            }
            is CreateQuestionEvent.SetNotEditableMode -> {
                val index = questions.indexOf(event.question)
                val isValid = runValidation(questions[index])
                if (isValid)
                    questions[index] = questions[index].copy(state = QuestionsViewMode.NonEditable)
            }
            is CreateQuestionEvent.SetCorrectOption -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(ansKey = event.option)
            }
            is CreateQuestionEvent.SubmitQuestions -> {
                if (ansKeyValidation()) {
                    viewModelScope.launch {
                        messages.emit(UiEvent.ShowSnackBar("Some ans-keys are not set, set them !"))
                    }
                    return
                }
                if (allQuestionValid()) onSubmit(event.quidId)
            }
        }
    }

    private fun runValidation(question: CreateQuestionState): Boolean {

        val questionValidation = validator.validateQuestion(question)
        val index = questions.indexOf(question)

        // this local variable helps to solve the problem with
        // java.util.ConcurrentModificationException
        // changing the option at one go will trigger the loop rendering all the options again
        // Thus creating the local variable and changing them at one go
        val optionErrors: MutableList<QuestionOptionsState> = mutableStateListOf()

        val optionsValidation = question.options.map { option ->
            val vOption = validator.validateOptions(option)
            optionErrors.add(
                QuestionOptionsState(
                    option = option.option,
                    optionError = if (!vOption.isValid) vOption.message else null
                )
            )
            vOption
        }
        questions[index] =
            question.copy(options = optionErrors, questionError = questionValidation.message)
        return questionValidation.isValid && optionsValidation.all { it.isValid }
    }

    private fun allQuestionValid() =
        questions.map { question -> runValidation(question) }.all { it }

    private fun ansKeyValidation() = questions.any { it.ansKey == null }

    private fun onSubmit(id: String) {
        val models = questions.map { it.toModel().copy(quizId = id) }
        viewModelScope.launch {
            repo.createQuestionsToQuiz(models).onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        showDialog.value = false
                        messages.emit(UiEvent.ShowSnackBar(res.message ?: ""))
                    }
                    is Resource.Success -> messages.emit(UiEvent.NavigateBack)
                    is Resource.Loading -> showDialog.value = true
                }
            }.launchIn(this)
        }
    }
}