package com.angel.core.data.datasource

import android.net.Uri

data class MediaStoreAudio(
    val id: Long,
    val uri: Uri,
    val title: String,
    val artist: String,
    val album: String,
    val albumId: Long,
    val durationMs: Long,
    val trackNumber: Int,
    val artworkUri: Uri
)