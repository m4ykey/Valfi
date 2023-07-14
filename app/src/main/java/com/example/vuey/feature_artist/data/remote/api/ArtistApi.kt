package com.example.vuey.feature_artist.data.remote.api

import com.example.vuey.feature_artist.data.remote.model.last_fm.ArtistLastFm
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtistApi {

    @GET("2.0/?method=artist.getinfo")
    suspend fun getArtistBio(
        @Query("artist") artistName : String,
        @Query("format") format : String = "json"
    ) : ArtistLastFm

}