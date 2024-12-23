package com.example.musicproject.di

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExoPlayerModule {
    @Provides
    @Singleton
    fun exoPlayerProvide(
        @ApplicationContext context: Context,
    ): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

}