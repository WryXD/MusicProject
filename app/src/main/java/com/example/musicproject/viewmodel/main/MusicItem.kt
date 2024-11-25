package com.example.musicproject.viewmodel.main

data class MusicItem(
    val id: Long,
    val title: String,
    val artist: String,
    val albumCoverUrl: String,
    val previewUrl: String
)
