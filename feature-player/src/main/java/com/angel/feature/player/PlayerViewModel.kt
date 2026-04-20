package com.angel.feature.player

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.SessionToken
import com.angel.core.data.repository.TrackRepository
import com.angel.core.player.service.MediaItemTree
import com.angel.core.player.service.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: TrackRepository
) : ViewModel() {

    private var browserFuture: ListenableFuture<MediaBrowser>? = null
    private val browser: MediaBrowser?
        get() = if (browserFuture?.isDone == true) browserFuture?.get() else null

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var progressJob: Job? = null

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        browserFuture = MediaBrowser.Builder(context, sessionToken)
            .setListener(object : MediaBrowser.Listener {
                override fun onChildrenChanged(
                    browser: MediaBrowser,
                    parentId: String,
                    itemCount: Int,
                    params: MediaLibraryService.LibraryParams?
                ) {
                    if (parentId == MediaItemTree.ALL_SONGS_ID || parentId == MediaItemTree.ROOT_ID) {
                        loadTracks()
                    }
                }
            })
            .buildAsync()
        browserFuture?.addListener({
            try {
                val browser = browserFuture?.get() ?: return@addListener
                setupPlayer(browser)
                browser.subscribe(MediaItemTree.ROOT_ID, null)
                browser.subscribe(MediaItemTree.ALL_SONGS_ID, null)
                loadTracks()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
    }

    private fun setupPlayer(player: Player) {
        player.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (events.containsAny(
                        Player.EVENT_MEDIA_ITEM_TRANSITION,
                        Player.EVENT_PLAYBACK_STATE_CHANGED,
                        Player.EVENT_PLAY_WHEN_READY_CHANGED
                    )
                ) {
                    updateState(player)
                }
            }
        })
        updateState(player)
    }

    private fun updateState(player: Player) {
        val currentMediaItem = player.currentMediaItem
        _uiState.update {
            it.copy(
                currentTrack = currentMediaItem?.toTrack(),
                isPlaying = player.isPlaying,
                duration = player.duration.coerceAtLeast(0L)
            )
        }

        if (player.isPlaying) {
            startProgressUpdate()
        } else {
            stopProgressUpdate()
        }
    }

    private fun startProgressUpdate() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                browser?.let { browser ->
                    _uiState.update { it.copy(position = browser.currentPosition) }
                }
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdate() {
        progressJob?.cancel()
    }

    fun loadTracks() {
        viewModelScope.launch {
            // Tell the repository to scan for tracks
            repository.refresh()
            
            val browser = browser ?: return@launch

            try {
                val rootResult = withContext(Dispatchers.Main) {
                    browser.getLibraryRoot(null)
                }.get()
                val root = rootResult.value ?: return@launch
                
                val childrenResult = withContext(Dispatchers.Main) {
                    browser.getChildren(root.mediaId, 0, Int.MAX_VALUE, null)
                }.get()
                val children = childrenResult.value ?: return@launch

                val allSongsFolder = children.find { it.mediaId == MediaItemTree.ALL_SONGS_ID }
                val tracksItems = if (allSongsFolder != null) {
                    val folderChildrenResult = withContext(Dispatchers.Main) {
                        browser.getChildren(allSongsFolder.mediaId, 0, Int.MAX_VALUE, null)
                    }.get()
                    folderChildrenResult.value ?: emptyList()
                } else {
                    children
                }

                _uiState.update { state ->
                    state.copy(tracks = tracksItems.map { it.toTrack() })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun playTrack(index: Int) {
        val browser = browser ?: return
        val tracks = _uiState.value.tracks
        if (index !in tracks.indices) return

        val mediaItems = tracks.map { it.toMediaItem() }
        browser.setMediaItems(mediaItems, index, 0L)
        browser.prepare()
        browser.play()
    }

    fun playPause() {
        val browser = browser ?: return
        if (browser.isPlaying) {
            browser.pause()
        } else {
            browser.play()
        }
    }

    fun next() {
        browser?.seekToNext()
    }

    fun previous() {
        browser?.seekToPrevious()
    }

    fun seek(position: Long) {
        browser?.seekTo(position)
    }

    override fun onCleared() {
        super.onCleared()
        browserFuture?.let {
            MediaBrowser.releaseFuture(it)
        }
    }
}

