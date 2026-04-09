package com.angel.audioflow.navigation

sealed class Routes(val route: String) {
    object Player : Routes("player")
    object Library : Routes("library")
}