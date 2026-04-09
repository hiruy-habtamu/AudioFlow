package com.angel.audioflow.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Routes.Library.route,
            onClick = {
                navController.navigate(Routes.Library.route) {
                    popUpTo(Routes.Library.route)
                    launchSingleTop = true
                }
            },
            label = { Text("library") },
            icon = { Icon(Icons.AutoMirrored.Default.List, contentDescription = null) }

        )
        NavigationBarItem(
            selected = currentRoute == Routes.Player.route,
            onClick = {
                navController.navigate(Routes.Player.route) {
                    popUpTo(Routes.Player.route)
                    launchSingleTop = true
                }
            },
            label = { Text("Player") },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) }
        )
    }
}