package com.m4ykey.data.remote.model.album

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.ImageDto

@Keep
data class ArtistAlbumItemDto(
    val album_group: String?,
    val id: String?,
    val images: List<ImageDto>?,
    val name: String?
)