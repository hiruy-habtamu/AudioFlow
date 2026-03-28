package com.angel.core.player

import com.angel.core.model.PlaybackState
import com.angel.core.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FakeAudioPlayer : AudioPlayer {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _currentTrack = MutableStateFlow<Track?>(null)
    override val currentTrack: StateFlow<Track?> = _currentTrack

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    override val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _currentPosition = MutableStateFlow(0L)
    override val currentPosition: StateFlow<Long> = _currentPosition

    private var queue: List<Track> = emptyList()
    private var currentIndex = 0

    private var progressJob: Job? = null

    private fun startProgress() {
        progressJob?.cancel()

        progressJob = scope.launch {
            while (_playbackState.value == PlaybackState.PLAYING) {
                delay(1000)
                _currentPosition.value += 1000

                val duration = _currentTrack.value?.duration ?: 0L

                if (_currentPosition.value >= duration) {
                    playNext()
                }
            }
        }
    }

    private fun playCurrent() {
        val track = queue.getOrNull(currentIndex) ?: return
        _currentPosition.value = 0L
        play(track)
    }

    override fun setQueue(
        tracks: List<Track>,
        startIndex: Int
    ) {
        queue = tracks
        currentIndex = startIndex
        playCurrent()
    }

    override fun play(track: Track) {
        _currentTrack.value = track
        _playbackState.value = PlaybackState.PLAYING
        startProgress()
    }

    override fun pause() {
        _playbackState.value = PlaybackState.PAUSED
        progressJob?.cancel()
    }

    override fun resume() {
        _playbackState.value = PlaybackState.PLAYING
        startProgress()
    }

    override fun seekTo(position: Long) {
        _currentPosition.value = position
    }

    override fun playNext() {
        if (currentIndex < queue.lastIndex) {
            currentIndex++
            playCurrent()
        }
    }

    override fun playPrevious() {
        if (currentIndex > 0) {
            currentIndex--
            playCurrent()
        }
    }


}