package com.example.musicproject.viewmodel.boarding

sealed interface BoardingEvent {
    data class Navigate(val route: String, val popUp: String? = null) : BoardingEvent
}