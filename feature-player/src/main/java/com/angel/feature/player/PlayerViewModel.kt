package com.angel.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.core.model.PlaybackState
import com.angel.core.model.Track
import com.angel.core.player.AudioPlayer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PlayerViewModel(
    private val player: AudioPlayer
) : ViewModel() {
    val uiState: StateFlow<PlayerUiState> = combine(
        player.currentTrack,
        player.playbackState,
        player.currentPosition
    ) { track, state, position ->
        PlayerUiState(
            currentTrack = track,
            isPlaying = state == PlaybackState.PLAYING,
            position = position
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