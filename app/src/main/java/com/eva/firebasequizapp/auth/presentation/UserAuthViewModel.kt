package com.eva.firebasequizapp.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.firebasequizapp.auth.data.UserAuthRepository
import com.eva.firebasequizapp.core.util.Resource
import com.eva.firebasequizapp.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val repository: UserAuthRepository
) : ViewModel() {

    private val inflow = MutableSharedFlow<UiEvent>()

    val events = inflow.asSharedFlow()

    fun signAnonymously() {
        viewModelScope.launch {
            when (val user = repository.signAnonymously()) {
                is Resource.Success -> {
                    inflow.emit(UiEvent.ShowSnackBar("LOgged in "))
                }
                is Resource.Error -> {
                    inflow.emit(
                        UiEvent.ShowDialog(
                            title = "Failed",
                            description = user.message ?: "Nothing"
                        )
                    )
                }
                else -> {

                }
            }
        }
    }
}