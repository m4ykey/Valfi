package com.lyrics.data.remote.api

import com.lyrics.data.remote.model.LyricsDtoItem
import retrofit2.http.GET
import retrofit2.http.Query

interface LyricsApi {

    @GET("search")
    suspend fun searchLyrics(
        @Query("track_name") trackName : String,
        @Query("artist_name") artistName : String
    ) : List<LyricsDtoItem>

}