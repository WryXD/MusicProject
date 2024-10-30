package com.example.musicproject.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicproject.repositories.auth.remote.AuthRepository
import com.example.musicproject.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _authChannel = Channel<AuthEvents>()
    val authEventsFlow = _authChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onAction(action: AuthActions) {
        when (action) {
            is AuthActions.Login -> {
                login()
            }

            AuthActions.Logout -> {
                logout()
            }

            AuthActions.Register -> {
                register()
            }

            is AuthActions.Reset -> {
                reset()
            }

            is AuthActions.UpdateEmail -> {
                viewModelScope.launch {
                    _authState.update { it.copy(email = action.email) }
                }
            }

            is AuthActions.UpdatePassword -> {
                viewModelScope.launch {
                    _authState.update { it.copy(password = action.password) }
                }
            }

            is AuthActions.UpdateName -> {
                viewModelScope.launch {
                    _authState.update { it.copy(name = action.name) }
                }
            }

            is AuthActions.UpdateSurName -> {
                viewModelScope.launch {
                    _authState.update { it.copy(surName = action.surName) }
                }
            }

            is AuthActions.UpdateIsLogin -> {
                viewModelScope.launch {
                    _authState.update { it.copy(isLogin = true) }
                }
            }

            is AuthActions.UpdateIsSignUp -> {
                _authState.update { it.copy(isSignUp = true) }
            }

            is AuthActions.IsSignUpValid -> {
                viewModelScope.launch {
                    val result = StringUtils.isEmailValid(action.email)
                    _authState.update { it.copy(isEnableButton = result) }
                }
            }

            AuthActions.OnNavigateTo -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isNavigate = true) }
                }
            }

            is AuthActions.OnSignUpScreen -> {
                viewModelScope.launch {
                    //_state.update { it.copy(email = action.email, password = action.password) }
                }
            }

            is AuthActions.OnBack -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isBack = true) }
                    delay(200)
                    resetUiState()
                }
            }

            is AuthActions.UpdateBirthday -> {
                viewModelScope.launch {
                    _authState.update { it.copy(birthday = action.birthday) }
                }
            }

            AuthActions.UpdateBackStatus -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isBack = false) }
                }
            }

            AuthActions.UpdateNavigationStatus -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isNavigate = false) }
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            repository.login(_authState.value.email, _authState.value.password)
            _authChannel.send(AuthEvents.Success)
        }
    }

    private fun register() {
        viewModelScope.launch {
            repository.register(_authState.value.email, _authState.value.password)
            _authChannel.send(AuthEvents.Success)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authChannel.send(AuthEvents.Success)
        }
    }

    private fun reset() {
        viewModelScope.launch {
            _authState.update { AuthState() }
            Log.e("AuthViewModel", "Reset: ${_authState.value}")
        }
    }

    private fun resetUiState() {
        viewModelScope.launch {
            _uiState.update { UiState() }
        }
    }
}