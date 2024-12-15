package com.m4ykey.authentication.interceptor.token

import android.util.Base64
import com.m4ykey.core.BuildConfig

fun generateToken() : String {
    val credentials = "${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}"
    val base64Credentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    return "Basic $base64Credentials"
}