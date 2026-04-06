package com.angel.core.data.repository

import com.angel.core.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTracks(): Flow<List<Track>>
}