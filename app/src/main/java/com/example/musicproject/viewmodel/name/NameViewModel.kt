package com.example.musicproject.viewmodel.name

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
class NameViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(NameState())
    val state = _state.asStateFlow()

    fun onAction(action: NameAction) {
        when (action) {

            is NameAction.OnBack -> onBack()

            is NameAction.OnNavigateTo -> onNavigateTo()

        }
    }

    private fun onNavigateTo() {
        viewModelScope.launch {
            _state.update { it.copy(isNavigate = true) }
            resetNavigationState()
        }
    }

    private fun onBack() {
        viewModelScope.launch {
            _state.update { it.copy(isBack = true) }
            resetBackState()
        }
    }


    private fun resetNavigationState() {
        viewModelScope.launch {
            delay(200)
            _state.update { it.copy(isNavigate = false) }
        }
    }

    private fun resetBackState() {
        viewModelScope.launch {
            delay(200)
            _state.update { it.copy(isBack = false) }
        }
    }
}