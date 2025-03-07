package com.m4ykey.data.remote.model.artist

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.album.ExternalUrlsDto

@Keep
data class ArtistDto(
    val external_urls: ExternalUrlsDto?,
    val name: String?,
    val id : String?
)