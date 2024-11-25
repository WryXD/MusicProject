package com.example.musicproject.viewmodel.main

import androidx.media3.exoplayer.ExoPlayer

data class MusicPlayerState(
    val title: String = "",
    val artist: String = "",
    val albumCoverUrl: String = "",
    val previewUrl: String = "",
    val currentPlayer: ExoPlayer? = null,
    val isPlaying: Boolean = false,
)
