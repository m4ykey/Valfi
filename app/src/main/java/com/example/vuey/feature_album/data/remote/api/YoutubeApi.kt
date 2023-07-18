package com.example.vuey.feature_album.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YoutubeApi {

    @GET("v3/videos/{id}")
    suspend fun getYoutubeVideo(
        @Path("id") youtubeId : String,
        @Query("part") part : String = "snippet,player"
    )

}