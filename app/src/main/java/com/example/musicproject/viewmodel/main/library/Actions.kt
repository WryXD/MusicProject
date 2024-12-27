package com.example.musicproject.viewmodel.main.library

interface Actions {
    data object OnShowCreatePlaylistDialog : Actions
    data object OnHideCreatePlaylistDialog : Actions
    data object PauseMusic: Actions
    data object ReleaseExoPlayer: Actions
    data object InitializeExoPlayer: Actions
    data object ListenToLikedSongUpdate : Actions
    data object ListenToPlaylistUpdate : Actions

    data class OnShowLikedSongPlaylist(val isShow: Boolean): Actions
    data class UpdatePlaylistName(val name: String) : Actions
    data class OnCreatePlaylist(val playlistName: String, val song: List<String>) : Actions
    data class PlayPlaylistTrack(val index: Int): Actions
    data class UpdateVisibleMusicPlayer(val isVisible: Boolean): Actions
    data class DeletedPlaylist(val id: String): Actions
}