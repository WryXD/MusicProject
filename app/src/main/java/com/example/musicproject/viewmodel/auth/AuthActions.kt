package com.example.musicproject.viewmodel.auth

sealed interface AuthActions {

    data object Login : AuthActions
    data object Register : AuthActions
    data object Logout : AuthActions
    data object Reset : AuthActions

    data class UpdateEmail(val email: String) : AuthActions
    data class UpdatePassword(val password: String) : AuthActions
    data class UpdateName(val name: String) : AuthActions
    data class UpdateSurName(val surName: String) : AuthActions
    data class UpdateBirthday(val birthday: String) : AuthActions

    data object UpdateIsLogin : AuthActions
    data object UpdateIsSignUp : AuthActions
    data object UpdateNavigationStatus : AuthActions
    data object UpdateBackStatus : AuthActions

    data object OnNavigateTo : AuthActions
    data object OnBack : AuthActions


    data class OnSignUpScreen(val email: String, val password: String) : AuthActions
    data class IsSignUpValid(val email: String) : AuthActions
}