package com.m4ykey.data.remote.model.album

import androidx.annotation.Keep

@Keep
data class AlbumsDto(
    val items: List<AlbumItemDto>?,
    val limit: Int?,
    val next: String?,
    val offset: Int,
    val previous: String?
)