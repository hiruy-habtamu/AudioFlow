package com.angel.feature.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
    val state: PlayerUiState by viewModel.uiState.collectAsState()

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

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.previous() }) {
                Text("⏮")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { viewModel.playPause() }) {
                Text(if (state.isPlaying) "⏸" else "▶️")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { viewModel.next() }) {
                Text("⏭")
            }

        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Position: ${state.position / 1000}s")
    }
}

@Composable
fun PlayerContent(
    state: PlayerUiState,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
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

        Text(text = "Position: ${state.position / 1000}s")

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
        onPrevious = {}
    )
}