package com.example.musicproject.viewmodel.main

import androidx.media3.exoplayer.ExoPlayer

data class MusicPlayerState(
    val id: Long = 0,
    val title: String = "",
    val artist: String = "",
    val albumCoverUrl: String = "",
    val previewUrl: String = "",
    val duration: Int = 0,
    val currentPlayer: ExoPlayer? = null,
    val isPlaying: Boolean = false,
    val previewDuration: Int? = null,
    val isVisible: Boolean = false,
    val currentIndex: Int = 0,
    val currentPlaylist: List<MusicItem> = emptyList(),
    val isReleaseExoPlayer: Boolean = false
)
