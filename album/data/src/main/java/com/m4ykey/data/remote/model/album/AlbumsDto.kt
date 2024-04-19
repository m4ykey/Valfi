package com.m4ykey.data.remote.model.album

data class AlbumsDto(
    val items: List<AlbumItemDto>?,
    val limit: Int?,
    val next: String?,
    val offset: Int,
    val previous: String?
)