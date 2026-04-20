package com.angel.core.data.repository

import com.angel.core.data.datasource.MediaStoreAudio
import com.angel.core.data.datasource.MediaStoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val dataSource: MediaStoreDataSource) : TrackRepository {
    override fun getTracks(): Flow<List<MediaStoreAudio>> = flow {
        val tracks = dataSource.getAudioFiles()
        emit(tracks)
    }
}