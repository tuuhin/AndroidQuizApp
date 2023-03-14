package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    val user: FirebaseUser?
) : ViewModel() {


    var questions = mutableStateListOf(CreateQuestionState())
        private set

    fun onQuestionEvent(event: CreateQuestionEvent) {
        when (event) {
            is CreateQuestionEvent.DescriptionAdded -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(
                    desc = event.desc
                )
            }
            is CreateQuestionEvent.OnOptionEvent -> {
                val index = questions.indexOf(event.question)
                when (event.optionEvent) {
                    OptionsEvent.OptionAdded -> {
                        questions[index].options.apply {
                            add(QuestionOptionsState())
                        }

                    }
                    is OptionsEvent.OptionRemove -> {
                        questions[index].options.apply {
                            removeAt(event.optionEvent.index)
                        }
                        if (questions[index].ansKey == questions[index].options[event.optionEvent.index]) {
                            questions[index] = questions[index].copy(ansKey = null)
                        }
                    }
                    is OptionsEvent.OptionValueChanged -> {
                        questions[index].options[event.optionEvent.index] =
                            questions[index].options[event.optionEvent.index].copy(
                                option = event.optionEvent.value
                            )
                    }
                }
            }
            CreateQuestionEvent.QuestionAdded -> {
                questions.apply {
                    add(CreateQuestionState())
                }
            }
            is CreateQuestionEvent.QuestionQuestionAdded -> {
                val index = questions.indexOf(event.question)
                if (index != -1)
                    questions[index] = questions[index].copy(
                        question = event.value
                    )
            }
            is CreateQuestionEvent.QuestionRemoved -> {
                questions.apply {
                    remove(event.question)
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
                questions[index] = questions[index].copy(
                    required = !questions[index].required
                )
            }
            is CreateQuestionEvent.SetEditableMode -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(
                    state = QuestionBaseState.Editable
                )
            }
            is CreateQuestionEvent.SetNotEditableMode -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(
                    state = QuestionBaseState.NonEditable
                )
            }
            is CreateQuestionEvent.SetCorrectOption -> {
                val index = questions.indexOf(event.question)
                questions[index] = questions[index].copy(
                    ansKey = event.option
                )
            }
           is  CreateQuestionEvent.ShuffleOptions -> {
               val index = questions.indexOf(event.question)
                questions[index].options.shuffle()
            }
        }
    }


}