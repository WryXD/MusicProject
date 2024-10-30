package com.example.musicproject.viewmodel.boarding

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
class BoardingViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(BoardingState())
    val state = _uiState.asStateFlow()

    fun onAction(action: BoardingActions) {
        when (action) {
            is BoardingActions.OnNavigateTo -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isSliding = false) }
                    _uiState.update { it.copy(isNavigate = true) }
                    delay(200)
                    reset()
                }
            }
        }
    }

    private fun reset() {
        viewModelScope.launch {
            _uiState.update { BoardingState() }
        }

    }
}