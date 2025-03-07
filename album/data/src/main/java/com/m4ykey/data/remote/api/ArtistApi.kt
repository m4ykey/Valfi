package com.m4ykey.data.remote.api

import com.m4ykey.data.remote.model.artist.ArtistDto
import com.m4ykey.data.remote.model.artist.ArtistResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ArtistApi {

    @GET("artists")
    suspend fun getSeveralArtists(
        @Header("Authorization") token : String,
        @Query("ids") ids : String
    ) : ArtistResponseDto

}