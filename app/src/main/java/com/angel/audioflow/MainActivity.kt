package com.angel.audioflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.angel.audioflow.ui.theme.AudioFlowTheme
import com.angel.core.player.FakeAudioPlayer
import com.angel.feature.player.PlayerScreen
import com.angel.feature.player.PlayerViewModel
import com.angel.core.model.Track

class MainActivity : ComponentActivity() {
    val tracks = listOf(
        Track("1", "Song A", "Artist A", "", 5000),
        Track("2", "Song B", "Artist B", "", 8000),
        Track("3", "Song C", "Artist C", "", 6000)
    )
    val player = FakeAudioPlayer()
    val viewModel = PlayerViewModel(player)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.playSample(tracks)
        enableEdgeToEdge()
        setContent {
            AudioFlowTheme {
                PlayerScreen(viewModel)
            }
        }
    }
}