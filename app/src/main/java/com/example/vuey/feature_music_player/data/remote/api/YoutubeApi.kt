package com.example.vuey.feature_music_player.data.remote.api

import com.example.vuey.BuildConfig
import com.example.vuey.feature_music_player.data.remote.model.name.MusicName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("v3/search?key=${BuildConfig.YOUTUBE_API_KEY}")
    suspend fun getVideoName(
        @Query("q") query : String,
        @Query("part") part : String = "snippet"
    ) : Response<MusicName>

}