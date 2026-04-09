package com.angel.core.data.di

import android.content.ContentResolver
import android.content.Context
import com.angel.core.data.datasource.MediaStoreDataSource
import com.angel.core.data.repository.TrackRepository
import com.angel.core.data.repository.TrackRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideMediaStoreDataSource(
        contentResolver: ContentResolver
    ): MediaStoreDataSource = MediaStoreDataSource(contentResolver)

    @Provides
    @Singleton
    fun provideTrackRepository(
        dataSource: MediaStoreDataSource
    ): TrackRepository = TrackRepositoryImpl(dataSource)
}