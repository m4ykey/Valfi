package com.lyrics.data.remote.api

import com.lyrics.data.remote.model.TrackDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TrackApi {

    @GET("tracks/{id}")
    suspend fun getTrackById(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ) : TrackDto

}