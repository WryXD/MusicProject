package com.example.musicproject.viewmodel.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicproject.data.dto.Data
import com.example.musicproject.repositories.music.MusicService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicService: MusicService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState = _uiState.asStateFlow()

    private val _musicData = MutableStateFlow<List<MusicItem>>(emptyList())
    val musicData = _musicData.asStateFlow()

    private val _musicPlayerData = MutableStateFlow<MusicPlayerState>(MusicPlayerState())
    val musicPlayerData = _musicPlayerData.asStateFlow()

    init {
        val query = generateSingleLetter().toString()
        fetchMusicData(query)
    }

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.UpdateBottomNavId -> selectedBottomNavigation(action.id)
            is MainScreenAction.PerformMusic -> performMusic(
                action.albumCoverUrl,
                action.artist,
                action.title,
                action.previewUrl
            )
            is MainScreenAction.UpdatePlayingState -> updatePlayingState()
        }
    }

    private fun fetchMusicData(query: String) {
        viewModelScope.launch {
            try {
                val response = musicService.getMusicData(query)
                if (response.isSuccessful) {
                    response.body()?.data?.let { musicData ->
                        _musicData.value = mapMusicData(musicData)
                        Log.e("MainViewModel", "Music data: $musicData")
                    }
                } else {
                    Log.e("MainViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun performMusic(
        albumCoverUrl: String,
        artist: String,
        title: String,
        preview: String,
    ) {
        viewModelScope.launch {
            _musicPlayerData.update {
                it.copy(
                    albumCoverUrl = albumCoverUrl,
                    artist = artist,
                    title = title,
                    previewUrl = preview
                )
            }
        }
    }

    private fun updatePlayingState() {
        _musicPlayerData.update {
            it.copy(isPlaying = !_musicPlayerData.value.isPlaying)
        }
    }

    private suspend fun mapMusicData(musicData: List<Data>): List<MusicItem> {
        return withContext(Dispatchers.IO) {
            musicData.map { data ->
                MusicItem(
                    id = data.id,
                    title = data.title,
                    artist = data.artist.name,
                    albumCoverUrl = data.album.cover_medium,
                    previewUrl = data.preview
                )
            }
        }
    }

    private fun generateSingleLetter(): Char {
        return ('a'..'z').random()
    }

    private fun selectedBottomNavigation(id: Int) {
        _uiState.update {
            it.copy(bottomNavId = id)
        }
    }
}