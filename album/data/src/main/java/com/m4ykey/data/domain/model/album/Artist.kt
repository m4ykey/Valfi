package com.m4ykey.data.domain.model.album

import androidx.annotation.Keep

@Keep
data class Artist(
    val name : String,
    val externalUrls: ExternalUrls,
    val id : String
)
