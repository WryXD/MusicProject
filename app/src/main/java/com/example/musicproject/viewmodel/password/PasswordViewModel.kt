package com.example.musicproject.viewmodel.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicproject.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(PasswordState())
    val state = _state.asStateFlow()

    fun onAction(action: PasswordActions) {
        when (action) {
            is PasswordActions.IsEnableButton -> {
                viewModelScope.launch {
                    val result = StringUtils.isPasswordValid(action.password)
                    _state.update { it.copy(isEnableButton = result) }
                }
            }

            is PasswordActions.IsShowingPassword -> {
                viewModelScope.launch {
                    _state.update { it.copy(isShowingPassword = !_state.value.isShowingPassword) }
                }
            }

            PasswordActions.OnBack -> {
                viewModelScope.launch {
                    _state.update { it.copy(isBack = true) }
                }
            }

            PasswordActions.OnNavigateTo -> {
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