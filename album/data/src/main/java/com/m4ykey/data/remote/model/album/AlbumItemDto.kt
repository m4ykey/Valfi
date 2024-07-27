package com.m4ykey.data.remote.model.album

import androidx.annotation.Keep

@Keep
data class AlbumItemDto(
    val artists: List<ArtistDto>?,
    val id: String?,
    val images: List<ImageDto>?,
    val name: String?,
    val album_type: String?
)