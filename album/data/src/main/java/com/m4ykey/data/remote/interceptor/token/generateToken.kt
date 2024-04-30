package com.m4ykey.data.remote.interceptor.token

import android.util.Base64

fun generateToken(clientId : String, clientSecret : String) : String {
    return "Basic " + Base64.encodeToString(
        "$clientId:$clientSecret".toByteArray(),
        Base64.NO_WRAP
    )
}