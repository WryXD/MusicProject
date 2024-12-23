package com.example.musicproject.di

import com.example.musicproject.repositories.music.MusicService
import com.example.musicproject.repositories.music.remote.MusicRepository
import com.example.musicproject.repositories.music.remote.MusicRepositoryImpl
import com.example.musicproject.utils.Constants.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMusicApi(retrofit: Retrofit): MusicService {
        return retrofit.create(MusicService::class.java)
    }

    @Singleton
    @Provides
    fun provideMusicRepository(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore,
    ): MusicRepository {
        return MusicRepositoryImpl(firebaseAuth, fireStore)
    }
}