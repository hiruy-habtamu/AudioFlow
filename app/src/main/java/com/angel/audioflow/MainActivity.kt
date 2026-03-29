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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioFlowTheme {
                PlayerScreen()
            }
        }
    }
}