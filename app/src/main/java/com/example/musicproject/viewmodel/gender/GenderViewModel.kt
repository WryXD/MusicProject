package com.example.musicproject.viewmodel.gender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor() : ViewModel() {
    private val _genderState = MutableStateFlow(GenderState())
    val genderState: StateFlow<GenderState> = _genderState.asStateFlow()

    fun onAction(genderAction: GenderAction) {
        when (genderAction) {
            is GenderAction.OnBack -> onBack()

            is GenderAction.OnNavigateTo -> navigateTo()

            is GenderAction.OnSelected -> onSelected()
        }
    }

    private fun onSelected() {
        viewModelScope.launch {
            _genderState.update { it.copy(isSelected = !_genderState.value.isSelected) }
        }
    }

    private fun navigateTo() {
        viewModelScope.launch {
            _genderState.update { it.copy(isNavigate = true) }
            resetNavigationState()
        }
    }

    private fun onBack() {
        viewModelScope.launch {
            _genderState.update { it.copy(isBack = true) }
            resetBackState()
        }
    }

    private fun resetNavigationState() {
        viewModelScope.launch {
            delay(200)
            _genderState.update { it.copy(isNavigate = false) }
        }

    }

    private fun resetBackState() {
        viewModelScope.launch {
            delay(200)
            _genderState.update { it.copy(isBack = false) }
        }

    }

}