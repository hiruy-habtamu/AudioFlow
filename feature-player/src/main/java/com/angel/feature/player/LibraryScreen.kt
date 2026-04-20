package com.angel.feature.player

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.angel.core.model.Track

@OptIn(ExperimentalMaterial3Api::class)
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { launcher.launch(permission) }) {
                Text("Grant Permission")
            }
        }
        return
    }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("My Library", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LibraryContent(
            state = state,
            onTrackClicked = { index -> viewModel.playTrack(index) },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun LibraryContent(
    state: PlayerUiState,
    onTrackClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.tracks.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp) // Space for a mini-player if added later
        ) {
            itemsIndexed(state.tracks) { index, track ->
                val isPlaying = track.id == state.currentTrack?.id
                
                TrackItem(
                    track = track,
                    isPlaying = isPlaying,
                    onClick = { onTrackClicked(index) }
                )
            }
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = if (isPlaying) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) 
                else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Artwork
            AsyncImage(
                model = track.artworkUri,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                error = rememberVectorPainter(if (isPlaying) Icons.Rounded.PlayArrow else Icons.Rounded.MusicNote),
                placeholder = rememberVectorPainter(if (isPlaying) Icons.Rounded.PlayArrow else Icons.Rounded.MusicNote)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isPlaying) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            if (isPlaying) {
                // simple animated-like indicator or just an icon
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LibraryContentPreview() {
    val fakeState = PlayerUiState(
        tracks = listOf(
            Track("1", "Starboy", "The Weeknd", "", 230000),
            Track("2", "Blinding Lights", "The Weeknd", "", 200000),
            Track("3", "Save Your Tears", "The Weeknd", "", 210000)
        ),
        currentTrack = Track("1", "Starboy", "The Weeknd", "", 230000),
        isPlaying = true
    )
    MaterialTheme {
        LibraryContent(
            state = fakeState,
            onTrackClicked = {}
        )
    }
}
