package com.example.musicproject.viewmodel.boarding

sealed interface BoardingActions {
    data object OnNavigateTo: BoardingActions
}