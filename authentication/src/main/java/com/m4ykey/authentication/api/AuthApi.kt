package com.m4ykey.authentication.api

import com.m4ykey.authentication.api.model.Auth
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("api/token")
    suspend  fun getAccessToken(
        @Header("Authorization") token : String,
        @Field("grant_type") grantType : String
    ) : Auth

}