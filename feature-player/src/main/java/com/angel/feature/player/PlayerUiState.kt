package com.angel.feature.player

import com.angel.core.model.Track

data class PlayerUiState(
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val position: Long = 0L
)
