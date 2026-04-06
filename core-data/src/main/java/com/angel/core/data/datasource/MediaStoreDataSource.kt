package com.angel.core.data.datasource

import android.content.ContentResolver
import android.provider.MediaStore

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
            MediaStore.Audio.Media.DURATION
        )

        val cursor = contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )
        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                list.add(
                    MediaStoreAudio(
                        id = it.getLong(idCol),
                        title = it.getString(titleCol),
                        artist = it.getString(artistCol),
                        duration = it.getLong(durationCol)
                    )
                )
            }
        }
        return list
    }
}