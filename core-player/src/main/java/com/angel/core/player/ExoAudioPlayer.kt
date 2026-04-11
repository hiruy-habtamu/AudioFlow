package com.angel.core.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.angel.core.model.PlaybackState
import com.angel.core.model.Track
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExoAudioPlayer(
    context: Context
) : AudioPlayer {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    private var queue: List<Track> = emptyList()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _currentTrack = MutableStateFlow<Track?>(null)
    override val currentTrack: StateFlow<Track?> = _currentTrack

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    override val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _currentPosition = MutableStateFlow(0L)
    override val currentPosition: StateFlow<Long> = _currentPosition

    private var progressJob: Job? = null

    init {
        setupListener()
    }


    private fun setupListener() {
        player.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.value =
                    if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED

                if (isPlaying) startProgress() else stopProgress()
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val index = player.currentMediaItemIndex
                _currentTrack.value = queue.getOrNull(index)
                _currentPosition.value = 0L
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    stopProgress()
                }
            }
        })
    }

    private fun startProgress() {
        progressJob?.cancel()

        progressJob = scope.launch {
            while (player.isPlaying) {
                _currentPosition.value = player.currentPosition
                delay(500)
            }
        }
    }

    private fun stopProgress() {
        progressJob?.cancel()
    }

    override fun setQueue(tracks: List<Track>, startIndex: Int) {
        queue = tracks

        val mediaItems = tracks.map {
            MediaItem.Builder()
                .setUri(it.uri)
                .setMediaId(it.id)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.title)
                        .setArtist(it.artist)
                        .build()
                ).build()
        }

        player.setMediaItems(mediaItems)
        player.prepare()

        player.seekTo(startIndex, 0L)
        player.play()
    }

    override fun play(track: Track) {
        val index = queue.indexOf(track)
        if (index != -1) {
            player.seekTo(index, 0L)
            player.play()
        }
    }

    override fun pause() {
        player.pause()
    }

    override fun resume() {
        player.play()
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun playNext() {
        player.seekToNextMediaItem()
    }

    override fun playPrevious() {
        player.seekToPreviousMediaItem()
    }

    fun release() {
        stopProgress()
        player.release()
        scope.cancel()
    }
}