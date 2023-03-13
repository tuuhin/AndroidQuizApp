package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.eva.firebasequizapp.quiz.domain.models.QuestionOptionsModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    val user: FirebaseUser?
) : ViewModel() {


    var questions = mutableStateListOf(CreateQuestionState())
        private set

//    var options = mutableStateListOf(QuestionOptionsModel())
//        private set
//
//    fun onOptionsEvent(event: OptionsEvent) {
//        when (event) {
//            is OptionsEvent.OptionAdded -> {
//                options.add(QuestionOptionsModel())
//            }
//            is OptionsEvent.OptionRemove -> {
//                options.removeAt(event.index)
//            }
//            is OptionsEvent.OptionValueChanged -> {
//                options[event.index] = options[event.index].copy(option = event.value)
//            }
//        }
//    }

    fun onQuestionEvent(event: CreateQuestionEvent) {
        when (event) {
            is CreateQuestionEvent.DescriptionAdded -> {
                questions[event.index] = questions[event.index].copy(
                    desc = event.desc
                )
            }
            is CreateQuestionEvent.OnOptionEvent -> {
                when (event.optionEvent) {
                    OptionsEvent.OptionAdded -> {
                        questions[event.index].options.apply {
                            add(QuestionOptionsModel())
                        }
                    }
                    is OptionsEvent.OptionRemove -> {
                        questions[event.index].options.apply {
                            removeAt(event.optionEvent.index)
                        }
                    }
                    is OptionsEvent.OptionValueChanged -> {
                        questions[event.index].options[event.optionEvent.index] =
                            questions[event.index].options[event.optionEvent.index].copy(
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
                questions[event.index] = questions[event.index].copy(
                    question = event.question
                )
            }
            is CreateQuestionEvent.QuestionRemoved -> {
                questions.apply {
                    removeAt(event.index)
                }
            }
            is CreateQuestionEvent.ToggleQuestionDesc -> {
                questions[event.index] = questions[event.index].copy(
                    desc = if (questions[event.index].desc == null) "" else null
                )
            }
            is CreateQuestionEvent.ToggleRequiredField -> {
                questions[event.index] = questions[event.index].copy(
                    required = !questions[event.index].required
                )
            }
        }
    }


}