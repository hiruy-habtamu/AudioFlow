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

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
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

//
//@Composable
//fun PlayerScreenPreview(viewModel: PlayerViewModel) {
//    val state: PlayerUiState by viewModel.uiState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = state.currentTrack?.title ?: "No Track",
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//        }
//    }
//}