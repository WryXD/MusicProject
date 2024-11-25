package com.example.musicproject.viewmodel.password

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
class PasswordViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(PasswordState())
    val state = _state.asStateFlow()

    fun onAction(action: PasswordAction) {
        when (action) {

            is PasswordAction.IsShowingPassword -> isShowingPassword()

            is PasswordAction.OnBack -> onBack()

            is PasswordAction.OnNavigateTo -> onNavigateTo()

            is PasswordAction.IsTrigger -> updateTrigger()

            is PasswordAction.ResetTrigger -> resetTrigger()

            is PasswordAction.Reset -> resetAll()
        }
    }

    private fun resetAll() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isBack = false,
                    isNavigate = false,
                    isTrigger = false,
                    isShowingPassword = false
                )
            }
        }
    }

    private fun isShowingPassword() {
        viewModelScope.launch {
            _state.update { it.copy(isShowingPassword = !_state.value.isShowingPassword) }
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

    private fun resetTrigger() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isTrigger = false
                )
            }
        }
    }

    private fun updateTrigger() {
        viewModelScope.launch {
            _state.update { it.copy(isTrigger = true) }
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