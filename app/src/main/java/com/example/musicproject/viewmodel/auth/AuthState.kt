package com.example.musicproject.viewmodel.auth

/**
 * Represents the current authentication mode (Login or Sign Up).
 * Used to control UI behavior and logic flow based on the user's selected action.
 */

data class AuthState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val surName: String = "",
    val isEmailValid: Boolean = false,
    val isLogin: Boolean = false,
    val isSignUp: Boolean = false,
    val isEnableButton: Boolean = false,
    val birthday: String? = null,
)
