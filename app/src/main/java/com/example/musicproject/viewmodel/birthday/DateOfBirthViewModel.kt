package com.example.musicproject.viewmodel.birthday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DateOfBirthViewModel @Inject constructor() : ViewModel() {
    private val _dobState = MutableStateFlow(DobState())
    val uiState = _dobState.asStateFlow()

    fun onAction(action: DobActions) {
        when (action) {
            is DobActions.OnBack -> {
                viewModelScope.launch {
                    _dobState.update { it.copy(isBack = true) }
                    resetBackState()
                }
            }

            is DobActions.OnNavigateTo -> {
                viewModelScope.launch {
                    _dobState.update { it.copy(isNavigate = true) }
                    resetNavigationState()
                }
            }

            is DobActions.UpdateVisibleDatePicker -> {
                viewModelScope.launch {
                    _dobState.update { it.copy(isVisibleDatePicker = !_dobState.value.isVisibleDatePicker) }
                }
            }

        }
    }

    private fun resetNavigationState() {
        viewModelScope.launch {
            delay(200)
            _dobState.update { it.copy(isNavigate = false) }
        }
    }

    private fun resetBackState() {
        viewModelScope.launch {
            delay(200)
            _dobState.update { it.copy(isBack = false) }
        }

    }
}