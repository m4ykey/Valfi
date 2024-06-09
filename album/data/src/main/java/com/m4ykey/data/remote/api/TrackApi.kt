package com.m4ykey.data.remote.api

import com.m4ykey.data.remote.model.tracks.TrackListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TrackApi {

    @GET("albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Header("Authorization") token : String,
        @Path("id") id : String,
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : TrackListDto

}