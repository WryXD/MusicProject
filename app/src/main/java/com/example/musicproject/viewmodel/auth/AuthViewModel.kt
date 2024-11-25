package com.example.musicproject.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicproject.repositories.auth.remote.AuthRepository
import com.example.musicproject.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _authenticationState = MutableStateFlow(AuthenticationState())
    val authenticationState = _authenticationState.asStateFlow()

    init {
        checkUserLoginState()
    }

    private fun checkUserLoginState() {
        viewModelScope.launch {
            val result = repository.checkCurrentUser()
            when {
                result.isSuccess -> updateToLoggedIn()
                result.isFailure -> updateToLoggedOut()
            }
        }
    }

    private fun updateToLoggedOut() {
        viewModelScope.launch {
            _authenticationState.update { it.copy(isLoggedIn = false) }
        }
    }

    private fun updateToLoggedIn() {
        viewModelScope.launch {
            _authenticationState.update { it.copy(isLoggedIn = true) }
        }
    }

    fun onAction(action: AuthActions) {
        when (action) {

            is AuthActions.Login -> login()

            is AuthActions.Logout -> logout()

            is AuthActions.Register -> register()

            is AuthActions.Reset -> resetAllAuthState()

            is AuthActions.ResetAuthState -> resetAuthState()

            is AuthActions.UpdateEmail -> updateEmail(action.email)

            is AuthActions.UpdatePassword -> updatePassword(action.password)

            is AuthActions.UpdateName -> updateName(action.name)

            is AuthActions.UpdateSurName -> updateSurName(action.surName)

            is AuthActions.UpdateGender -> updateGender(action.gender)

            is AuthActions.UpdateCurrentGender -> updateCurrentGender(action.gender)

            is AuthActions.UpdateBirthDay -> updateBirthDay(action.birthday)

            is AuthActions.UpdateIsLogin -> updateIsLogin()

            is AuthActions.UpdateIsSignUp -> updateIsSignUp()

            is AuthActions.OnNavigateTo -> navigateTo()

            is AuthActions.OnBack -> onBack()

            is AuthActions.UpdateBackStatus -> updateBackStatus()

            is AuthActions.UpdateNavigationStatus -> updateNavigationStatus()

            is AuthActions.OnShowLoading -> showLoadingAnimation()

            is AuthActions.OnHideLoading -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }
        }
    }

    private fun hideLoadingAnimation() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = false) }
        }
    }

    private fun updateGender(gender: String) {
        viewModelScope.launch {
            _authState.update { it.copy(gender = gender) }
        }
    }

    private fun updateNavigationStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isNavigate = false) }
        }
    }

    private fun updateBackStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBack = false) }
        }
    }

    private fun updateBirthDay(birthday: String) {
        viewModelScope.launch {
            _authState.update { it.copy(birthday = birthday) }
        }
    }

    private fun onBack() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBack = true) }
            delay(200)
            resetUiState()
        }
    }

    private fun navigateTo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isNavigate = true) }
            resetNavigationState()
        }
    }

    private fun resetNavigationState() {
        viewModelScope.launch {
            delay(200)
            _uiState.update { it.copy(isNavigate = false) }
        }
    }

    private fun updateIsSignUp() {
        _authState.update { it.copy(isSignUp = true) }
    }

    private fun updateSurName(surName: String) {
        viewModelScope.launch {
            _authState.update { it.copy(surName = surName) }
        }
    }


    private fun updateIsLogin() {
        viewModelScope.launch {
            _authState.update { it.copy(isLogin = true) }
        }
    }

    private fun updateName(name: String) {
        viewModelScope.launch {
            _authState.update { it.copy(name = name) }
        }
    }

    private fun updatePassword(password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(password = password) }
        }

    }

    private fun updateEmail(email: String) {
        viewModelScope.launch {
            _authState.update { it.copy(email = email) }
        }

    }

    private fun updateCurrentGender(gender: String) {
        viewModelScope.launch {
            val currentGender = _authState.value.currentGender?.toMutableMap() ?: mutableMapOf()

            currentGender.keys.forEach { key ->
                currentGender[key] = false
            }

            currentGender[gender] = true
            _authState.update { it.copy(currentGender = currentGender) }
        }
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {

            val result = repository.login(_authState.value.email, _authState.value.password)

            when {
                result.isSuccess -> {
                    _authState.update { it.copy(isSuccess = true) }
                    _authenticationState.update { it.copy(isLoggedIn = true) }
                }

                result.isFailure -> _authState.update { it.copy(isFailed = true) }
            }
        }
    }

    private fun register() {
        viewModelScope.launch(Dispatchers.IO) {

            val result = repository.register(
                _authState.value.email,
                _authState.value.password,
                "${_authState.value.name} ${_authState.value.surName}",
                _authState.value.birthday!!,
                _authState.value.gender!!
            )

            when {
                result.isSuccess -> {
                    _authState.update { it.copy(isSuccess = true) }
                    _authenticationState.update { it.copy(isLoggedIn = true) }
                }

                result.isFailure -> _authState.update { it.copy(isFailed = true) }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
        }
    }

    fun enableNameButton(): Boolean {
        viewModelScope.launch {
            if (StringUtils.isFullNameValid(_authState.value.name, _authState.value.surName)) {
                _authState.update {
                    it.copy(isEnableButton = true)
                }
            } else {
                _authState.update {
                    it.copy(isEnableButton = false)
                }
            }
        }
        return _authState.value.isEnableButton
    }

    fun enableBirthDayButton(): Boolean {
        viewModelScope.launch {
            if (_authState.value.birthday?.isNotEmpty() == true) {
                _authState.update {
                    it.copy(isEnableButton = true)
                }
            } else {
                _authState.update {
                    it.copy(isEnableButton = false)
                }
            }
        }
        return _authState.value.isEnableButton
    }

    fun enablePasswordButton(): Boolean {
        viewModelScope.launch {
            if (StringUtils.isPasswordValid(_authState.value.password)) {
                _authState.update {
                    it.copy(isEnableButton = true)
                }
            } else {
                _authState.update {
                    it.copy(isEnableButton = false)
                }
            }
        }
        return _authState.value.isEnableButton
    }

    fun enableEmailButton(): Boolean {
        viewModelScope.launch {
            val result = StringUtils.isEmailValid(_authState.value.email)
            if (result) {
                _authState.update { it.copy(isEnableButton = true) }
            } else {
                _authState.update { it.copy(isEnableButton = false) }
            }
        }
        return _authState.value.isEnableButton
    }

    private fun resetUiState() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isBack = false,
                    isNavigate = false
                )
            }
        }
    }

    private fun resetAuthState() {
        viewModelScope.launch {
            _authState.update {
                it.copy(
                    isLogin = false,
                    isSignUp = false,
                    isEnableButton = false,

                    email = "",
                    password = "",
                    name = "",
                    surName = "",
                    isEmailValid = false,
                    birthday = null,
                    gender = null,
                    currentGender = null,

                    isLoading = false,
                    isSuccess = false,
                    isFailed = false,
                )
            }
        }
    }

    private fun resetAllAuthState() {
        viewModelScope.launch {
            _authState.update {
                it.copy(
                    isLogin = false,
                    isSignUp = false,
                    isEnableButton = false,

                    email = "",
                    password = "",
                    name = "",
                    surName = "",
                    isEmailValid = false,
                    birthday = null,
                    gender = null,
                    currentGender = null,

                    isLoading = false,
                    isSuccess = false,
                    isFailed = false,
                )
            }
            _authenticationState.update {
                it.copy(
                    isLoggedIn = false
                )
            }

            _uiState.update {
                it.copy(
                    isBack = false,
                    isNavigate = false
                )
            }
        }

    }
}