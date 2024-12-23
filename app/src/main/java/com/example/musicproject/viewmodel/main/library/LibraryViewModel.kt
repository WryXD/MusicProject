package com.example.musicproject.viewmodel.main.library

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicproject.repositories.music.MusicService
import com.example.musicproject.repositories.music.remote.MusicRepository
import com.example.musicproject.repositories.music.remote.Song
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
class LibraryViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val musicService: MusicService,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _libraryState = MutableStateFlow(MusicLibraryState())
    val library = _libraryState.asStateFlow()

    private val _createPlaylistState = MutableStateFlow(CreatePlaylistState())
    val createPlaylistState = _createPlaylistState.asStateFlow()

    private var exoPlayer: ExoPlayer? = null

    private fun initializeExoPlayer() {
        releaseExoPlayer()
        exoPlayer = ExoPlayer.Builder(getApplication(context)).build()
        _libraryState.update { it.copy(isReleaseExoplayer = false) }
    }

    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
        _libraryState.update { it.copy(isReleaseExoplayer = true) }
    }

    fun onAction(action: Actions) {
        when (action) {
            is Actions.OnShowCreatePlaylistDialog -> showCreatePlaylistDialog()

            is Actions.OnShowLikedSongPlaylist -> showLikedSongPlaylist(action.isShow)

            is Actions.OnHideCreatePlaylistDialog -> hideCreatePlaylistDialog()

            is Actions.UpdatePlaylistName -> updatePlaylistName(action.name)

            is Actions.OnCreatePlaylist -> createPlayList(action.playlistName, action.song)

            is Actions.PlayPlaylistTrack -> playPlaylistTrack(action.index)

            is Actions.PauseMusic -> pauseMusic()

            is Actions.ReleaseExoPlayer -> releaseExoPlayer()

            is Actions.InitializeExoPlayer -> initializeExoPlayer()

            is Actions.UpdateVisibleMusicPlayer -> updateVisibleMusicPlayer(action.isVisible)

            is Actions.ListenToLikedSongUpdate -> listenToLikedSongsUpdates()

            is Actions.ListenToPlaylistUpdate -> listenToPlaylistUpdates()
        }
    }

    private fun showLikedSongPlaylist(isShowing: Boolean) {
       _libraryState.update { it.copy(isShowingLikedPlaylist = isShowing) }
    }

    private fun updateVisibleMusicPlayer(isVisible: Boolean) {
        _libraryState.update { it.copy(isVisible = isVisible) }
    }

    private fun listenToLikedSongsUpdates() {
        viewModelScope.launch {
            _libraryState.update { it.copy(isLoading = true) }
            musicRepository.getLikedSongsStream().collect { result ->
                result.onSuccess { songs ->
                    setPlaylist(songs)
                    _libraryState.update { it.copy(songs = songs, isLoading = false) }
                }.onFailure { error ->
                    _libraryState.update { it.copy(error = error.message, isLoading = false) }
                }
            }
        }
    }

    private fun createPlayList(playlistName: String, song: List<String>) {
        viewModelScope.launch {
            musicRepository.createPlayList(playlistName, song).fold(
                onSuccess = {
                    hideCreatePlaylistDialog()
                }, onFailure = { error ->
                    hideCreatePlaylistDialog()
                    Log.e("CreatePlayList", "Failed to create playlist: ${error.message}")
                }
            )
        }

    }

    private fun listenToPlaylistUpdates() {
        viewModelScope.launch {
            musicRepository.getPlayListStream().collect { result ->
                result.onSuccess { playlist ->
                    Log.d("PlaylistUpdated", "Playlist updated: $playlist")
                    _libraryState.update { it.copy(playList = playlist) }
                }.onFailure { error ->
                    _libraryState.update { it.copy(error = error.message) }
                }
            }
        }
    }

    private fun showCreatePlaylistDialog() {
        _createPlaylistState.update { it.copy(isShowing = true, playlistName = "") }
    }

    private fun hideCreatePlaylistDialog() {
        _createPlaylistState.update { it.copy(isShowing = false, playlistName = "") }
    }

    private fun updatePlaylistName(playlistName: String) {
        _createPlaylistState.update { it.copy(playlistName = playlistName) }
    }

    private fun setPlaylist(playlist: List<Song>) {
        _libraryState.update { it.copy(currentPlaylist = emptyList()) }
        _libraryState.update { it.copy(currentPlaylist = playlist) }
    }

    private fun playPlaylistTrack(index: Int) {
        viewModelScope.launch {

            val currentExoPlayer = exoPlayer ?: run {
                initializeExoPlayer()
                exoPlayer!!
            }

            _libraryState.update { it.copy(currentIndex = index, isVisible = true) }
            currentExoPlayer.stop()
            _libraryState.update { it.copy(isPlaying = false, previewDuration = null)}
                val currentPlayer = _libraryState.value.currentPlayer

            currentPlayer?.let { exoPlayer ->
                if (exoPlayer.isPlaying) {
                    exoPlayer.stop()
                    _libraryState.update { it.copy(isPlaying = false, previewDuration = null) }
                }
            }

            val currentPlaylist = _libraryState.value.currentPlaylist
            val track = currentPlaylist[index]
            val url = getTrack(track.songId.toLong())

            if (url == null) {
                Log.e("LibraryViewModel", "Failed to fetch track preview")
                return@launch
            }

            val mediaItem = MediaItem.fromUri(url)

            currentExoPlayer.apply {
                clearMediaItems()
                setMediaItem(mediaItem)
                prepare()
                removeListener(playerListener)
                addListener(playerListener)
                play()
            }

            // Update player state
            _libraryState.update {
                it.copy(
                    albumCoverUrl = track.songAlbumCoverUrl,
                    artist = track.songArtist,
                    title = track.songTitle,
                    currentPlayer = currentExoPlayer,
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
                        _libraryState.update { it.copy(previewDuration = durationMs.toInt()) }
                    }
                }

                Player.STATE_ENDED -> {
                    _libraryState.update {
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
            val currentIndex = _libraryState.value.currentIndex
            Log.e("currentIndex", currentIndex.toString())
            if (currentIndex < _libraryState.value.currentPlaylist.size - 1) {
                delay(300)
                playPlaylistTrack(currentIndex + 1)
            }
            if (currentIndex == _libraryState.value.currentPlaylist.size - 1) {
                delay(300)
                playPlaylistTrack(0)
            }
        }
    }

    fun playPrevious() {
        if (_libraryState.value.currentIndex > 0) {
            playPlaylistTrack(_libraryState.value.currentIndex - 1)
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


    private fun pauseMusic() {
        viewModelScope.launch {
            val isPlaying = _libraryState.value.currentPlayer

            isPlaying?.apply {
                if (isPlaying.isPlaying) {
                    isPlaying.pause()
                    _libraryState.update { it.copy(isPlaying = false) }
                } else {
                    isPlaying.play()
                    _libraryState.update { it.copy(isPlaying = true) }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releaseExoPlayer()
    }

}