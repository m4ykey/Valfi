package com.m4ykey.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Url

interface LyricsApi {

    @GET
    suspend fun getPage(@Url url : String) : String

}