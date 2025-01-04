package com.lyrics.data.remote.api

import com.lyrics.data.remote.model.LyricsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LyricsApi {

    @GET("search")
    suspend fun searchLyrics(
        @Query("q") query : String? = null,
        @Query("track_name") trackName : String? = null,
        @Query("artist_name") artistName : String? = null,
        @Query("album_name") albumName : String? = null
    ) : LyricsDto

}