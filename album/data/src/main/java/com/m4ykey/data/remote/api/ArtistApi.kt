package com.m4ykey.data.remote.api

import com.m4ykey.data.remote.model.album.ArtistDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ArtistApi {

    @GET("artists/{ids}")
    suspend fun getSeveralArtists(
        @Header("Authorization") token : String,
        @Path("ids") ids : String
    ) : List<ArtistDto>

}