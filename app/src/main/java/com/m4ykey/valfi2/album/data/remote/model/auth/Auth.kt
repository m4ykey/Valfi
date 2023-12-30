package com.m4ykey.valfi2.album.data.remote.model.auth

data class Auth(
    val access_token : String,
    val token_type : String,
    val expires_in : Int
)
