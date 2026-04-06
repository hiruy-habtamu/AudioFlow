package com.angel.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.core.data.repository.TrackRepository
import com.angel.core.model.PlaybackState
import com.angel.core.model.Track
import com.angel.core.player.AudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: AudioPlayer,
    private val repository: TrackRepository
) : ViewModel() {

    fun loadTracks() {
        viewModelScope.launch {
            repository.getTracks().collect { tracks ->
                if (tracks.isNotEmpty()) {
                    tracksFlow.value = tracks
                    player.setQueue(tracks)
                }
            }
        }
    }

    private val tracksFlow = MutableStateFlow<List<Track>>(emptyList())

    val uiState: StateFlow<PlayerUiState> = combine(
        player.currentTrack,
        player.playbackState,
        player.currentPosition,
        tracksFlow
    ) { track, state, position, tracks ->
        PlayerUiState(
            currentTrack = track,
            isPlaying = state == PlaybackState.PLAYING,
            position = position,
            tracks = tracks
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerUiState()
    )

    fun playSample(tracks: List<Track>) {
        player.setQueue(tracks)
    }

    fun playPause() {

        if (player.playbackState.value == PlaybackState.PLAYING) {
            player.pause()
        } else {
            player.resume()
        }
    }

    fun next() {
        player.playNext()
    }

    fun previous() {
        player.playPrevious()
    }

    fun seek(position: Long) {
        player.seekTo(position)
    }
}