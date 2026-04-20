package com.angel.core.data.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.core.net.toUri

class MediaStoreDataSource(
    private val contentResolver: ContentResolver
) {
    fun getAudioFiles(): List<MediaStoreAudio> {
        val list = mutableListOf<MediaStoreAudio>()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TRACK
        )

        val cursor = contentResolver.query(
            uri,
            projection,
            MediaStore.Audio.Media.IS_MUSIC,
            null,
            null
        )
        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val album = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val trackNumber = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val albumId = it.getLong(albumIdCol)
                list.add(
                    MediaStoreAudio(
                        id = id,
                        title = it.getString(titleCol),
                        artist = it.getString(artistCol),
                        durationMs = it.getLong(durationCol),
                        uri = ContentUris.withAppendedId(uri, id),
                        album = it.getString(album),
                        albumId = albumId,
                        trackNumber = it.getInt(trackNumber),
                        artworkUri = "content://media/external/audio/albumart/$albumId".toUri()
                    )
                )
            }
        }
        return list
    }
}