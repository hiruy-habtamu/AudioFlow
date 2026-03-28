package com.angel.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val uri: String,
    val duration: Long
)