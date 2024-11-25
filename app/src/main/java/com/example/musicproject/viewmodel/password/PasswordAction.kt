package com.example.musicproject.viewmodel.password

sealed interface PasswordAction {
    data object IsShowingPassword: PasswordAction
    data object OnBack: PasswordAction
    data object OnNavigateTo: PasswordAction
    data object IsTrigger: PasswordAction
    data object ResetTrigger: PasswordAction
    data object Reset: PasswordAction
}