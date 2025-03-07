package com.m4ykey.data.remote.model.album

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.artist.ArtistDto

@Keep
data class AlbumItemDto(
    val artists: List<ArtistDto>?,
    val id: String?,
    val images: List<ImageDto>?,
    val name: String?,
    val album_type: String?
)