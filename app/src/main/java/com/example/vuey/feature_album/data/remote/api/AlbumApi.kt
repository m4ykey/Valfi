package com.example.vuey.feature_album.data.remote.api

import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail
import com.example.vuey.feature_album.data.remote.model.spotify.album.SearchAlbum
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApi {

    @GET("v1/search")
    suspend fun searchAlbum(
        @Query("q") query : String,
        @Query("type") type : String = "album",
        @Header("Authorization") token : String,
        @Query("limit") limit : Int = 50,
        @Query("offset") offset : Int = 0
    ) : SearchAlbum

    @GET("v1/albums/{id}")
    suspend fun getAlbum(
        @Path("id") albumId : String,
        @Header("Authorization") token : String
    ) : AlbumDetail

}