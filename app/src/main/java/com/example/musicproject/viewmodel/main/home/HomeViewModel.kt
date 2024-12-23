package com.example.musicproject.viewmodel.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicproject.domain.model.user.UserProfile
import com.example.musicproject.repositories.auth.remote.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserProfile())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnFetchUserProfile -> fetchUserProfile()
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            authRepository.getUserProfile().fold(
                onSuccess = { userProfile ->
                    _uiState.value = userProfile
                    Log.d(
                        "HomeViewModel", "User profile retrieved successfully: $userProfile"
                    )
                },
                onFailure = { exception ->
                    Log.e("HomeViewModel", "Failed to get user profile", exception)
                }
            )
        }
    }

    fun getFirstName(): Char {
        return _uiState.value.name.firstOrNull() ?: ' '
    }

}