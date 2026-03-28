package com.angel.player

import com.angel.model.PlaybackState
import com.angel.model.Track
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayer {

    //Controls
    fun play(track: Track)
    fun pause()
    fun resume()
    fun seekTo(position: Long)

    fun playNext()
    fun playPrevious()

    //Queue
    fun setQueue(tracks: List<Track>, startIndex: Int = 0)

    //State
    val currentTrack: StateFlow<Track?>
    val playbackState: StateFlow<PlaybackState>
    val currentPosition: StateFlow<Long>
}
