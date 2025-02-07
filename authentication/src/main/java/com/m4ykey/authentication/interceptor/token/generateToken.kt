package com.m4ykey.authentication.interceptor.token

import android.util.Base64

fun generateToken(clientId : String, clientSecret: String) : String {
    val credentials = "$clientId:$clientSecret"
    val base64Credentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    return "Basic $base64Credentials"
}