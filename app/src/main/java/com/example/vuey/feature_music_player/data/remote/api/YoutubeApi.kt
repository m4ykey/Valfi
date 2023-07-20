package com.example.vuey.feature_music_player.data.remote.api

import com.example.vuey.feature_music_player.data.remote.model.name.YoutubeSearch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YoutubeApi {

    @GET("v3/search")
    suspend fun getYoutubeVideoByName(
        @Query("q") query : String,
        @Query("part") part : String = "snippet",
        @Query("maxResults") maxResults : Int = 1,
        @Query("type") type : String = "video"
    ) : YoutubeSearch


    @GET("v3/videos/{id}")
    suspend fun getYoutubeVideoById(
        @Path("id") youtubeId : String,
        @Query("part") part : String = "snippet,player"
    )

}