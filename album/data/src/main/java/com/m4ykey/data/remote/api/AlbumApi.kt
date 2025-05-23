package com.m4ykey.data.remote.api

import com.m4ykey.data.remote.model.album.AlbumDetailDto
import com.m4ykey.data.remote.model.album.AlbumListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApi {

    @GET("search")
    suspend fun searchAlbums(
        @Header("Authorization") token : String,
        @Query("q") query : String,
        @Query("type") type : String = "album,artist",
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : AlbumListDto

    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ) : AlbumDetailDto

    @GET("browse/new-releases")
    suspend fun getNewReleases(
        @Query("limit") limit : Int,
        @Query("offset") offset : Int,
        @Header("Authorization") token : String
    ) : AlbumListDto

}