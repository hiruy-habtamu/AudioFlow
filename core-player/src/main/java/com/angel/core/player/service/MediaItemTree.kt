package com.angel.core.player.service

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.angel.core.data.datasource.MediaStoreAudio

object MediaItemTree {
    private var mediaItems = mutableListOf<MediaItem>()

    const val ROOT_ID = "[rootID]"
    const val ALL_SONGS_ID = "[allSongsID]"

    fun initialize(tracks: List<MediaStoreAudio>) {
        mediaItems = tracks.map { track ->
            MediaItem.Builder()
                .setMediaId(track.id.toString())
                .setUri(track.uri)
                .setRequestMetadata(
                    MediaItem.RequestMetadata.Builder()
                        .setMediaUri(track.uri)
                        .build()
                )
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist)
                        .setAlbumTitle(track.album)
                        .setArtworkUri(track.artworkUri)
                        .setIsPlayable(true)
                        .setIsBrowsable(false)
                        .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                        .build()
                )
                .build()
        }.toMutableList()
    }

    fun getRootItem(): MediaItem {
        return MediaItem.Builder()
            .setMediaId(ROOT_ID)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setIsPlayable(false)
                    .setIsBrowsable(true)
                    .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                    .build()
            )
            .build()
    }

    fun getChildren(parentId: String): List<MediaItem>? {
        return when (parentId) {
            ROOT_ID -> listOf(
                MediaItem.Builder()
                    .setMediaId(ALL_SONGS_ID)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("All Songs")
                            .setIsPlayable(false)
                            .setIsBrowsable(true)
                            .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_ALBUMS)
                            .build()
                    )
                    .build()
            )
            ALL_SONGS_ID -> mediaItems
            else -> null
        }
    }

    fun getItem(mediaId: String): MediaItem? {
        return when (mediaId) {
            ROOT_ID -> getRootItem()
            ALL_SONGS_ID -> getChildren(ROOT_ID)?.first()
            else -> mediaItems.find { it.mediaId == mediaId }
        }
    }
}
