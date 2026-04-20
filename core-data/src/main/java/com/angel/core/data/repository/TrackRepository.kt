package com.angel.core.data.repository

import com.angel.core.data.datasource.MediaStoreAudio
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTracks(): Flow<List<MediaStoreAudio>>
    fun refresh()
}