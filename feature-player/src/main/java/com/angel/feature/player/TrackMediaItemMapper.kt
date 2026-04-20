package com.angel.feature.player

import androidx.media3.common.MediaItem
import com.angel.core.model.Track

fun MediaItem.toTrack(): Track {
    return Track(
        id = mediaId,
        title = mediaMetadata.title?.toString() ?: "Unknown",
        artist = mediaMetadata.artist?.toString() ?: "Unknown",
        uri = requestMetadata.mediaUri?.toString() ?: "",
        duration = 0,
        artworkUri = mediaMetadata.artworkUri?.toString()
    )
}

fun Track.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(id)
        .setUri(uri)
        .setMediaMetadata(
            androidx.media3.common.MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artist)
                .setArtworkUri(artworkUri?.let { android.net.Uri.parse(it) })
                .build()
        )
        .build()
}
