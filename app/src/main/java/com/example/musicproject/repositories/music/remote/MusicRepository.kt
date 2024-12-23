package com.example.musicproject.repositories.music.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun addLikedSong(
        songId: String,
        songTitle: String,
        songArtist: String,
        songAlbumCoverUrl: String,
    ): Result<Unit>

    suspend fun getLikedSongsStream(): Flow<Result<List<Song>>>
    suspend fun getLikedSongs(): Result<List<Song>>

    suspend fun createPlayList(
        playListName: String,
        song: List<String> = emptyList(),
    ): Result<Unit>

    suspend fun getPlayListStream(): Flow<Result<List<PlayList>>>
    suspend fun getPlayList(): Result<List<PlayList>>


}