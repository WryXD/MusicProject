package com.example.musicproject.viewmodel.name

sealed interface NameAction {
    data object OnBack: NameAction
    data object OnNavigateTo: NameAction
}