package com.example.musicproject.viewmodel.password

sealed interface PasswordActions {
    data class IsEnableButton(val password: String) : PasswordActions
    data object IsShowingPassword: PasswordActions
    data object OnBack: PasswordActions
    data object OnNavigateTo: PasswordActions
}