package com.eva.firebasequizapp.contribute_quiz.presentation

import androidx.compose.runtime.mutableStateListOf
import com.eva.firebasequizapp.quiz.domain.models.QuestionOptionsModel

data class CreateQuestionState(
    val question: String = "",
    val questionError: String? = null,
    val image: String? = null,
    val desc: String? = null,
    val required: Boolean = false,
    val isEditable: Boolean = true,
    val isAnswerKeyProvided: Boolean = false,
    // The option index better to keep it this way
    val ansKey: Int? = null,
    val options: MutableList<QuestionOptionsModel> = mutableStateListOf(QuestionOptionsModel())
)

data class QuestionOptionsState(
    val option: String = "",
    val isAns: Boolean = false
)


sealed class CreateQuestionEvent {
    object QuestionAdded : CreateQuestionEvent()
    data class ToggleQuestionDesc(val index: Int) : CreateQuestionEvent()
    data class QuestionRemoved(val index: Int) : CreateQuestionEvent()
    data class DescriptionAdded(val desc: String? = null, val index: Int) : CreateQuestionEvent()
    data class OnOptionEvent(val optionEvent: OptionsEvent, val index: Int) : CreateQuestionEvent()
    data class QuestionQuestionAdded(val question: String, val index: Int) : CreateQuestionEvent()
    data class ToggleRequiredField(val index: Int) : CreateQuestionEvent()
}

sealed class OptionsEvent {
    object OptionAdded : OptionsEvent()
    data class OptionValueChanged(val value: String, val index: Int) : OptionsEvent()
    data class OptionRemove(val index: Int) : OptionsEvent()
}
