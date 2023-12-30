package com.m4ykey.valfi2.album.data.remote.api

import com.m4ykey.valfi2.album.data.remote.model.auth.Auth
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("api/token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("grant_type") grantType : String,
        @Header("Authorization") auth : String
    ) : Auth

}