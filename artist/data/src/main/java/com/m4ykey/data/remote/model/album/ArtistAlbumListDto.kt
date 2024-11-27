package com.m4ykey.data.remote.model.album

import androidx.annotation.Keep

@Keep
data class ArtistAlbumListDto(
    val items: List<ArtistAlbumItemDto>?
)