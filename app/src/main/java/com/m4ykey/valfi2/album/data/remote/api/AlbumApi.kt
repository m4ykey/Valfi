package com.m4ykey.valfi2.album.data.remote.api

import com.m4ykey.valfi2.album.data.remote.model.album.AlbumListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface AlbumApi {

    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Header("Authorization") auth : String,
        @Path("id") id : String
    )

    @GET("albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Header("Authorization") auth: String,
        @Path("id") id : String,
        @Query("limit") limit : Int,
        @Query("offset") offset : Int,
        @Query("market") market : String = Locale.getDefault().country
    )

    @GET("search")
    suspend fun searchAlbums(
        @Header("Authorization") auth : String,
        @Query("q") query : String = "a",
        @Query("limit") limit : Int,
        @Query("offset") offset : Int,
        @Query("type") type : String = "album"
    ) : AlbumListDto

}