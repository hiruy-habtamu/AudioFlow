package com.angel.core.data.repository

import com.angel.core.data.datasource.MediaStoreAudio
import com.angel.core.data.datasource.MediaStoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onStart

class TrackRepositoryImpl(private val dataSource: MediaStoreDataSource) : TrackRepository {
    private val refreshSignal = MutableSharedFlow<Unit>(replay = 1)

    override fun getTracks(): Flow<List<MediaStoreAudio>> = flow {
        refreshSignal.onStart { emit(Unit) }.collect {
            emit(dataSource.getAudioFiles())
        }
    }

    override fun refresh() {
        refreshSignal.tryEmit(Unit)
    }
}
