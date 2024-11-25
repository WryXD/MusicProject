package com.example.musicproject.viewmodel.main

interface MainScreenAction {
    data class UpdateBottomNavId(val id: Int) : MainScreenAction
    data class PerformMusic(
        val title: String,
        val artist: String,
        val albumCoverUrl: String,
        val previewUrl: String,
    ) : MainScreenAction

    data object UpdatePlayingState : MainScreenAction
}