package com.angel.feature.player

import com.angel.core.model.Track

data class PlayerUiState(
    val tracks: List<Track> = emptyList(),
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L
)
