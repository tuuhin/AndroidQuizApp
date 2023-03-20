package com.eva.firebasequizapp.contribute_quiz.presentation

data class QuestionOptionsState(
    val option: String = "",
    val optionError: String? = null,
)

sealed class OptionsEvent {
    object OptionAdded : OptionsEvent()
    data class OptionValueChanged(val value: String, val index: Int) : OptionsEvent()
    data class OptionRemove(val option: QuestionOptionsState) : OptionsEvent()
}
