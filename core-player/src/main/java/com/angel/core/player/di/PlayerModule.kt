package com.angel.core.player.di


import com.angel.core.player.AudioPlayer
import com.angel.core.player.FakeAudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideAudioPlayer(): AudioPlayer {
        return FakeAudioPlayer()
    }
}