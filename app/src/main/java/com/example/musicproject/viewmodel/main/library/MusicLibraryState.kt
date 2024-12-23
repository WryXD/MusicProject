package com.example.musicproject.viewmodel.main.library

import androidx.media3.exoplayer.ExoPlayer
import com.example.musicproject.repositories.music.remote.PlayList
import com.example.musicproject.repositories.music.remote.Song

data class MusicLibraryState(
    val songs: List<Song> = emptyList(),
    val playList: List<PlayList> = emptyList(),
    val preview: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPlaylist: List<Song> = emptyList(),
    val currentIndex: Int = 0,
    val isVisible: Boolean = false,
    val currentPlayer: ExoPlayer? = null,
    val isPlaying: Boolean = false,
    val previewDuration: Int? = null,
    val albumCoverUrl: String = "",
    val artist: String = "",
    val title: String = "",
    val isReleaseExoplayer: Boolean = false,
    val isShowingLikedPlaylist: Boolean = false,
)
