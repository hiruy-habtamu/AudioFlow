package com.angel.audioflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.angel.audioflow.navigation.AppNavGraph
import com.angel.audioflow.ui.theme.AudioFlowTheme
import com.angel.feature.player.PlayerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioFlowTheme {
                AppNavGraph()
            }
        }
    }
}