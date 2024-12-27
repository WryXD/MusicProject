package com.example.musicproject.repositories.music.remote

data class PlayList(
    val playListId: String = "",
    val playListName: String = "",
    val songs: List<Song> = emptyList(),
)
