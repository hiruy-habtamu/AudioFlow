package com.angel.player

import com.angel.model.PlaybackState
import com.angel.model.Track
import kotlinx.coroutines.flow.StateFlow

class FakeAudioPlayer: AudioPlayer{

    override fun play(track: Track) {

        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun seekTo(position: Long) {
        TODO("Not yet implemented")
    }

    override fun playNext() {
        TODO("Not yet implemented")
    }

    override fun playPrevious() {
        TODO("Not yet implemented")
    }

    override fun setQueue(
        tracks: List<Track>,
        startIndex: Int
    ) {
        TODO("Not yet implemented")
    }

    override val currentTrack: StateFlow<Track?>
        get() = TODO("Not yet implemented")
    override val playbackState: StateFlow<PlaybackState>
        get() = TODO("Not yet implemented")
    override val currentPosition: StateFlow<Long>
        get() = TODO("Not yet implemented")

}