package com.example.musicproject.viewmodel.auth

sealed interface AuthEvents{
    data object Error: AuthEvents
    data object Success: AuthEvents
    data object Loading: AuthEvents
}
