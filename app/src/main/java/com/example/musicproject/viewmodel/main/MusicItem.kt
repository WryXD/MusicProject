package com.example.musicproject.viewmodel.main

import com.example.musicproject.data.dto.Artist

data class MusicItem(
    val id: Long,
    val title: String,
    val artist: String,
    val albumCoverUrl: String,
    val previewUrl: String,
    val duration: Int,
    val artistId: Long
)
