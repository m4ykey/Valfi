package com.m4ykey.data.remote.model.artist

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.album.ExternalUrlsDto
import com.m4ykey.data.remote.model.album.ImageDto

@Keep
data class ArtistListDto(
    val images : List<ImageDto>?,
    val external_urls: ExternalUrlsDto?,
    val name: String?,
    val id : String?
)
