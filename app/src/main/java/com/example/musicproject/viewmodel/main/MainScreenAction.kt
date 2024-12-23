package com.example.musicproject.viewmodel.main

interface MainScreenAction {
    data class UpdateBottomNavId(val id: Int) : MainScreenAction
    data class PlayTrack(
        val id: Long,
        val title: String,
        val artist: String,
        val albumCoverUrl: String,
    ) : MainScreenAction
    data class PlayPlaylistTrack(val index: Int): MainScreenAction
    data class AddLikedSong(
        val id: Long,
        val title: String,
        val artist: String,
        val albumCoverUrl: String,
    ) : MainScreenAction
    data class UpdateVisibleMusicPlayer(val isVisible: Boolean) : MainScreenAction

    data object PauseMusic : MainScreenAction
    data object ShowBottomSheet : MainScreenAction
    data object HideBottomSheet : MainScreenAction
    data object TriggerShowDrawer : MainScreenAction
    data object ReleaseExoPlayer : MainScreenAction
    data object InitializeExoPlayer : MainScreenAction


}