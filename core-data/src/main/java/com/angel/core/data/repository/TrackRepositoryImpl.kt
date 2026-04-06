package com.angel.core.data.repository

import com.angel.core.data.datasource.MediaStoreDataSource
import com.angel.core.data.mapper.toTrack
import com.angel.core.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val dataSource: MediaStoreDataSource) : TrackRepository {
    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = dataSource.getAudioFiles().map { it.toTrack() }
        emit(tracks)
    }
}