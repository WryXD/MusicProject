package com.example.musicproject.repositories.music

import com.example.musicproject.data.dto.Artist
import com.example.musicproject.data.dto.MusicData
import com.example.musicproject.data.dto.Track
import com.example.musicproject.utils.Constants.API_HOST
import com.example.musicproject.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicService {
    @Headers(API_KEY, API_HOST)
    @GET("search")
    suspend fun getMusicData(@Query("q") query: String): Response<MusicData>

    @Headers(API_KEY, API_HOST)
    @GET("track/{id}")
    suspend fun getTrackData(@Path("id") id: Long): Response<Track>

    @Headers(API_KEY, API_HOST)
    @GET("artist/{id}")
    suspend fun getArtistData(@Path("id") id: Long): Response<Artist>
}