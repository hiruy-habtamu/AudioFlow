package com.angel.feature.player


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    PlayerContent(
        state = state,
        onPlayPause = viewModel::playPause,
        onNext = viewModel::next,
        onPrevious = viewModel::previous,
        onSeek = viewModel::seek
    )
}

@Composable
fun PlayerContent(
    state: PlayerUiState,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.currentTrack?.title ?: "No Track",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = state.currentTrack?.artist ?: "Unknown",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onPrevious) {
                Text("⏮")
            }

            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onPlayPause) {
                Text(if (state.isPlaying) "⏸" else "▶️")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onNext) {
                Text("⏭")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        val duration = state.currentTrack?.duration ?: 0L
        val position = state.position

        Slider(
            value = if (duration > 0) position.toFloat() / duration else 0f,
            onValueChange = { progress ->
                val newPosition = (progress * duration).toLong()
                onSeek(newPosition)
            },
            modifier = Modifier
                .fillMaxWidth(0.67f)
                .padding(horizontal = 10.dp)
        )

        Text(
            text = "${position / 1000}s / ${duration / 1000}s"
        )
        Spacer(modifier = Modifier.height(24.dp))


    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerContentPreview() {
    val fakeState = PlayerUiState(
        currentTrack = com.angel.core.model.Track(
            id = "1",
            title = "Sample Song",
            artist = "Sample Artist",
            uri = "",
            duration = 5000
        ),
        isPlaying = true,
        position = 2000
    )

    PlayerContent(
        state = fakeState,
        onPlayPause = {},
        onNext = {},
        onPrevious = {},
        onSeek = {}
    )
}