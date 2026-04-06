package com.angel.core.data.mapper

import android.net.Uri
import android.provider.MediaStore
import com.angel.core.data.datasource.MediaStoreAudio
import com.angel.core.model.Track

fun MediaStoreAudio.toTrack(): Track {
    val uri = Uri.withAppendedPath(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        id.toString()
    )
    return Track(
        id = id.toString(),
        title = title,
        artist = artist,
        uri = uri.toString(),
        duration = duration
    )
}