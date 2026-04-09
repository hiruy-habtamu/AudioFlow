package com.angel.audioflow.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angel.feature.player.LibraryScreen
import com.angel.feature.player.PlayerScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        BottomBar(navController)
    }) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Library.route,
            modifier = Modifier.padding(padding)
        ){
            composable(Routes.Library.route){
                LibraryScreen()
            }
            composable(Routes.Player.route){
                PlayerScreen()
            }
        }
    }
}