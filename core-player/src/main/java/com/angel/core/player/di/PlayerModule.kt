package com.angel.core.player.di


import android.content.Context
import com.angel.core.player.AudioPlayer
import com.angel.core.player.ExoAudioPlayer
import com.angel.core.player.FakeAudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

//    @Provides
//    @Singleton
//    fun provideAudioPlayer(): AudioPlayer {
//        return FakeAudioPlayer()
//    }

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context) : AudioPlayer{
        return ExoAudioPlayer(context)
    }

}