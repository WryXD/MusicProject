package com.example.musicproject.viewmodel.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicproject.data.dto.Data
import com.example.musicproject.repositories.music.MusicService
import com.example.musicproject.repositories.music.remote.MusicRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicService: MusicService,
    private val musicRepository: MusicRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState = _uiState.asStateFlow()

    private val _musicData = MutableStateFlow<List<MusicItem>>(emptyList())
    val musicData = _musicData.asStateFlow()

    private val _musicPlayerData = MutableStateFlow(MusicPlayerState())
    val musicPlayerData = _musicPlayerData.asStateFlow()

    private var exoPlayer: ExoPlayer? = null

    init {
        initializeExoPlayer()
        val query = generateSingleLetter().toString()
        fetchMusicData(query)
    }

    private fun initializeExoPlayer() {
        releaseExoPlayer()
        exoPlayer = ExoPlayer.Builder(getApplication(context)).build()
        _musicPlayerData.update { it.copy(isReleaseExoPlayer = false) }
    }

    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
        _musicPlayerData.update { it.copy(isReleaseExoPlayer = true) }
    }

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.UpdateBottomNavId -> selectedBottomNavigation(action.id)

            is MainScreenAction.PlayTrack -> playTrack(
                action.id,
                action.albumCoverUrl,
                action.artist,
                action.title,
            )

            is MainScreenAction.PauseMusic -> pauseMusic()

            is MainScreenAction.ShowBottomSheet -> showBottomSheet()

            is MainScreenAction.HideBottomSheet -> hideBottomSheet()

            is MainScreenAction.AddLikedSong -> addLikedSong(
                action.id.toString(),
                action.title,
                action.artist,
                action.albumCoverUrl,
            )

            is MainScreenAction.TriggerShowDrawer -> openDrawer()

            is MainScreenAction.PlayPlaylistTrack -> playPlaylistTrack(action.index)

            is MainScreenAction.ReleaseExoPlayer -> releaseExoPlayer()

            is MainScreenAction.InitializeExoPlayer -> initializeExoPlayer()

            is MainScreenAction.UpdateVisibleMusicPlayer -> updateVisibleMusicPlayer(action.isVisible)
        }
    }

    private fun updateVisibleMusicPlayer(isVisible: Boolean){
        _musicPlayerData.update { it.copy(isVisible = isVisible) }
    }

    private fun showBottomSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.update { it.copy(isShowBottomSheet = true) }
        }
    }

    private fun hideBottomSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.update { it.copy(isShowBottomSheet = false) }
        }
    }

    private fun fetchMusicData(query: String) {
        viewModelScope.launch {
            try {
                val response = musicService.getMusicData(query)
                if (response.isSuccessful) {
                    response.body()?.data?.let { musicData ->
                        Log.e("MusicData", "MusicData: $musicData")
                        _musicData.value = mapMusicData(musicData)
                        setPlaylist(_musicData.value)
                        Log.e(
                            "MainViewModel",
                            "Music data fetched successfully: ${_musicData.value}"
                        )
                    }
                } else {
                    Log.e("MainViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun playTrack(
        id: Long,
        albumCoverUrl: String,
        artist: String,
        title: String,
    ) {
        viewModelScope.launch {
            val previewUrl = getTrack(id)
            if (previewUrl == null) {
                Log.e("performMusic", "Failed to fetch track preview")
                return@launch
            }

            val mediaItem = MediaItem.fromUri(previewUrl)
            _musicPlayerData.update { it.copy(isVisible = true) }
            val currentPlayer = _musicPlayerData.value.currentPlayer

            currentPlayer?.let { exoPlayer ->
                if (exoPlayer.isPlaying) {
                    exoPlayer.stop()
                    _musicPlayerData.update { it.copy(isPlaying = false, previewDuration = null) }
                }
            }
            val playerListener = object : Player.Listener{
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            val durationMs = exoPlayer?.duration
                            if (durationMs != null) {
                                _musicPlayerData.update { it.copy(previewDuration = durationMs.toInt()) }
                            }
                        }
                        Player.STATE_ENDED -> {
                            _musicPlayerData.update {
                                it.copy(
                                    previewDuration = null,
                                    isPlaying = false
                                )
                            }
                        }
                        Player.STATE_BUFFERING -> {
                            // Handle buffering state if needed
                        }
                        Player.STATE_IDLE -> {
                            // Handle idle state if needed
                        }
                    }
                }
            }
            exoPlayer?.apply {
                clearMediaItems()
                setMediaItem(mediaItem)
                prepare()
                removeListener(playerListener)
                addListener(playerListener)
                play()
            }

            // Update player state
            _musicPlayerData.update { musicData ->
                musicData.copy(
                    albumCoverUrl = albumCoverUrl,
                    artist = artist,
                    title = title,
                    currentPlayer = exoPlayer,
                    isPlaying = true
                )
            }
        }
    }

    private fun setPlaylist(playlist: List<MusicItem>) {
        _musicPlayerData.update { it.copy(currentPlaylist = emptyList()) }
        _musicPlayerData.update { it.copy(currentPlaylist = playlist) }
    }

    private fun playPlaylistTrack(index: Int) {
        viewModelScope.launch {
            // Update current index first
            _musicPlayerData.update { it.copy(currentIndex = index, isVisible = true) }

            val currentPlayer = _musicPlayerData.value.currentPlayer

            currentPlayer?.let { exoPlayer ->
                if (exoPlayer.isPlaying) {
                    exoPlayer.stop()
                    _musicPlayerData.update { it.copy(isPlaying = false, previewDuration = null) }
                }
            }

            val currentPlaylist = _musicPlayerData.value.currentPlaylist
            val track = currentPlaylist[index]

            val url = getTrack(track.id)
            if (url == null) {
                Log.e("performMusic", "Failed to fetch track preview")
                return@launch
            }
            _musicPlayerData.update { it.copy(isPlaying = false, previewDuration = null) }

            val mediaItem = MediaItem.fromUri(url)

            exoPlayer?.apply {
                clearMediaItems()
                setMediaItem(mediaItem)
                prepare()
                removeListener(playerListener)
                addListener(playerListener)
                play()
            }

            // Update player state
            _musicPlayerData.update { musicData ->
                musicData.copy(
                    albumCoverUrl = track.albumCoverUrl,
                    artist = track.artist,
                    title = track.title,
                    currentPlayer = exoPlayer,
                    isPlaying = true
                )
            }
        }
    }

    // Define the player listener outside the function to avoid adding it multiple times
    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    val durationMs = exoPlayer?.duration
                    if (durationMs != null) {
                        _musicPlayerData.update { it.copy(previewDuration = durationMs.toInt()) }
                    }
                }
                Player.STATE_ENDED -> {
                    _musicPlayerData.update {
                        it.copy(
                            previewDuration = null,
                            isPlaying = false
                        )
                    }
                    playNext()
                }
                Player.STATE_BUFFERING -> {
                    // Handle buffering state if needed
                }
                Player.STATE_IDLE -> {
                    // Handle idle state if needed
                }
            }
        }
    }

    fun playNext() {
        viewModelScope.launch {
            val currentIndex = _musicPlayerData.value.currentIndex
            Log.e("currentIndex", currentIndex.toString())
            if (currentIndex < _musicPlayerData.value.currentPlaylist.size - 1) {
                delay(300)
                playPlaylistTrack(currentIndex + 1)
            }
            if (currentIndex == _musicPlayerData.value.currentPlaylist.size - 1) {
                delay(300)
                playPlaylistTrack(0)
            }
        }
    }


    fun playPrevious() {
        if (_musicPlayerData.value.currentIndex > 0) {
            playPlaylistTrack(_musicPlayerData.value.currentIndex - 1)
        }
    }

    private suspend fun getTrack(id: Long): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = musicService.getTrackData(id)
                if (response.isSuccessful) {
                    response.body()?.preview
                } else {
                    Log.e("LibraryViewModel", "Error: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("LibraryViewModel", "Exception: ${e.message}")
                null
            }
        }
    }

    private fun addLikedSong(
        songId: String,
        songTitle: String,
        songArtist: String,
        songAlbumCoverUrl: String,
    ) {
        viewModelScope.launch {
            val result =
                musicRepository.addLikedSong(
                    songId = songId,
                    songTitle = songTitle,
                    songArtist = songArtist,
                    songAlbumCoverUrl = songAlbumCoverUrl,
                )

            if (result.isSuccess) {
                _uiState.update { it.copy(isAddLikedSong = true) }
                Log.e("AddLikedSong", "Add song successfully")
            } else {
                Log.e("AddLikedSong", "Failed to add song: ${result.exceptionOrNull()?.message}")
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
                    albumCoverUrl = data.album.cover_xl,
                    previewUrl = data.preview,
                    duration = data.duration,
                    artistId = data.artist.id.toLong()
                )
            }
        }
    }

    private fun pauseMusic() {
        viewModelScope.launch {
            val isPlaying = _musicPlayerData.value.currentPlayer

            isPlaying?.apply {
                if (isPlaying.isPlaying) {
                    isPlaying.pause()
                    _musicPlayerData.update { it.copy(isPlaying = false) }
                } else {
                    isPlaying.play()
                    _musicPlayerData.update { it.copy(isPlaying = true) }
                }
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

    private fun openDrawer() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDrawerOpen = true) }
            delay(300)
            _uiState.update { it.copy(isDrawerOpen = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _musicPlayerData.value.currentPlayer?.release()
    }

}