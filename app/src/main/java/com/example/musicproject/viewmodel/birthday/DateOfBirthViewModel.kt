package com.example.musicproject.viewmodel.birthday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DateOfBirthViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: Actions) {
        when (action) {
            is Actions.OnBack -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isBack = true) }
                }
            }

            is Actions.OnNavigateTo -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isNavigate = true) }
                }
            }

            is Actions.UpdateVisibleDatePicker -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isVisibleDatePicker = !_uiState.value.isVisibleDatePicker) }
                }
            }

            is Actions.OnEnableButton -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isEnableButton = true) }
                }
            }
        }
    }
}