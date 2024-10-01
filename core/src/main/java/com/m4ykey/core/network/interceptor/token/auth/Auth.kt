package com.m4ykey.core.network.interceptor.token.auth

import androidx.annotation.Keep

@Keep
data class Auth(
    val access_token : String?,
    val token_type : String?,
    val expires_in : Int?
)
