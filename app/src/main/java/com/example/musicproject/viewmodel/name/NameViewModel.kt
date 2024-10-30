package com.example.musicproject.viewmodel.name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicproject.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(NameState())
    val state = _state.asStateFlow()

    fun onAction(action: NameActions) {
        when (action) {
            is NameActions.IsEnableButton -> {
                viewModelScope.launch {
                    val result = StringUtils.isFullNameValid(action.name, action.surName)
                    _state.update { it.copy(isEnableButton = result) }
                }
            }

            is NameActions.OnBack -> {
                viewModelScope.launch {
                    _state.update { it.copy(isBack = true) }
                }
            }

            is NameActions.OnNavigateTo -> {
                viewModelScope.launch {
                    _state.update { it.copy(isNavigate = true) }
                    resetNavigationState()
                }
            }
        }
    }

    private fun resetNavigationState() {
        viewModelScope.launch {
            delay(200)
            _state.update { it.copy(isNavigate = false) }
        }
    }
}