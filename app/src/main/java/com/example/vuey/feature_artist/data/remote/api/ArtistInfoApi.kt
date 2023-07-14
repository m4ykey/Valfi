package com.example.vuey.feature_artist.data.remote.api

import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ArtistInfoApi {

    @GET("v1/artists/{id}")
    suspend fun getArtistInfo(
        @Path("id") artistId : String,
        @Header("Authorization") token : String
    ) : ArtistInfo

}