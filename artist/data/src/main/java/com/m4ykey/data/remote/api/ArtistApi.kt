package com.m4ykey.data.remote.api

import com.m4ykey.data.remote.model.album.ArtistAlbumListDto
import com.m4ykey.data.remote.model.ArtistDto
import com.m4ykey.data.remote.model.top_tracks.TopTrackListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistApi {

    @GET("artists/{id}")
    suspend fun getArtist(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ) : ArtistDto

    @GET("artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ) : TopTrackListDto

    @GET("artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Header("Authorization") token : String,
        @Path("id") id : String,
        @Query("limit") limit : Int,
        @Query("offset") offset : Int,
        @Query("include_groups") includeGroups : String
    ) : ArtistAlbumListDto

}