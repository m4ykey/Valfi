package com.m4ykey.data.domain.model.artist

import com.m4ykey.data.domain.model.album.ExternalUrls

data class Artist(
    val name : String,
    val externalUrls: ExternalUrls,
    val id : String
)
