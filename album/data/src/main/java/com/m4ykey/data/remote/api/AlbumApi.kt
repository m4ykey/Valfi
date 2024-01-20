package com.m4ykey.data.remote.api

import com.m4ykey.data.remote.model.album.AlbumDetailDto
import com.m4ykey.data.remote.model.album.SearchAlbumDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApi {

    @GET("search")
    suspend fun searchAlbums(
        @Header("Authorization") token : String,
        @Query("q") query : String,
        @Query("type") type : String = "album",
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : SearchAlbumDto

    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ) : AlbumDetailDto

}