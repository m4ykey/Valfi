package com.m4ykey.data.remote.model.album

import androidx.annotation.Keep

@Keep
data class ArtistDto(
    val external_urls: ExternalUrlsDto?,
    val name: String?,
    val id : String?
)