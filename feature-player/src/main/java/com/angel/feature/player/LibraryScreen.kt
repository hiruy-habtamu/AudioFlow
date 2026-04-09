package com.angel.feature.player

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.angel.core.model.Track

@Composable
fun LibraryScreen(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    var hasPermission by remember { mutableStateOf(false) }

    val permission = if (Build.VERSION.SDK_INT >= 33) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasPermission = granted
            if (granted) {
                viewModel.loadTracks()
            }
        }

    LaunchedEffect(Unit) {
        launcher.launch(permission)
    }

    if (!hasPermission) {
        Text("Permission required to load music")
        return
    }

    val state by viewModel.uiState.collectAsState()

    LibraryContent(state) { index -> viewModel.playTrack(index) }
}

@Composable
fun LibraryContent(
    state: PlayerUiState,
    onTrackClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 40.dp)
    ) {
        itemsIndexed(state.tracks) { index, track ->

            val isPlaying = track.id == state.currentTrack?.id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isPlaying)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            MaterialTheme.colorScheme.background
                    )
                    .clickable { onTrackClicked(index) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isPlaying) {
                    Text(
                        text = "▶",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Column {
                    Text(
                        text = track.title,
                        style = if (isPlaying)
                            MaterialTheme.typography.titleMedium
                        else
                            MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = track.artist,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LibraryContentPreview() {
    val fakeState = PlayerUiState(
        currentTrack = Track(
            id = "1",
            title = "Sample Song",
            artist = "Sample Artist",
            uri = "",
            duration = 5000
        ),
        isPlaying = true,
        position = 2000,
        tracks = listOf(
            Track("1", "Song A", "Artist A", "uri1", 11),
            Track("2", "Song B", "Artist B", "uri22", 100),
            Track("3", "Song C", "Artist C", "uri33", 44)
        )
    )
    LibraryContent(
        state = fakeState,
        onTrackClicked = {}
    )
}