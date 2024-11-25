package com.example.musicproject.di

import com.example.musicproject.repositories.music.MusicService
import com.example.musicproject.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    @Singleton
    @Provides
    fun provideMusicApi(retrofit: Retrofit): MusicService {
        return retrofit.create(MusicService::class.java)
    }
}