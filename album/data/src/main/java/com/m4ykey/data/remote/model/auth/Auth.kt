package com.m4ykey.data.remote.model.auth

import com.squareup.moshi.Json

data class Auth(
    @Json(name = "access_token") val accessToken : String,
    @Json(name = "token_type") val tokenType : String,
    @Json(name = "expires_in") val expiresIn : Int
)
