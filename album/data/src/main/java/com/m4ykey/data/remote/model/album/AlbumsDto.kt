package com.m4ykey.data.remote.model.album

data class AlbumsDto(
    val items: List<AlbumItemDto>,
    val limit: Int? = 0,
    val next: String? = "",
    val offset: Int? = 0,
    val previous: String? = ""
)