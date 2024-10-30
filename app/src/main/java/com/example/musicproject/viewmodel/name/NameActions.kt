package com.example.musicproject.viewmodel.name

import androidx.navigation.NavController

sealed interface NameActions {
    data class IsEnableButton(val name: String, val surName: String) : NameActions

    data object OnBack: NameActions
    data object OnNavigateTo: NameActions
}