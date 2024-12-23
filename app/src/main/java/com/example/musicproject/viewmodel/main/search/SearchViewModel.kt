package com.example.musicproject.viewmodel.main.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicproject.data.dto.Data
import com.example.musicproject.repositories.music.MusicService
import com.example.musicproject.viewmodel.main.MusicItem
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicRepository: MusicService,
    private var exoPlayer: ExoPlayer,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow(Search())
    val searchQuery = _searchQuery.asStateFlow()

    private val _musicData = MutableStateFlow<List<MusicItem>>(emptyList())
    val musicData = _musicData.asStateFlow()

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.Search -> search(action.query)

            is SearchAction.ExoPlayerState -> {}
        }
    }

    private fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery.update {
                it.copy(query = query)
            }
            getMusicData(query)
        }
    }

    private fun getMusicData(query: String) {
        viewModelScope.launch {
            try {
                val response = musicRepository.getMusicData(query)
                if (response.isSuccessful) {
                    response.body()?.data?.let { musicData ->
                        _musicData.value = mapMusicData(musicData)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
                    previewUrl = data.preview,
                    duration = data.duration,
                    artistId = data.artist.id.toLong()
                )
            }
        }
    }


}