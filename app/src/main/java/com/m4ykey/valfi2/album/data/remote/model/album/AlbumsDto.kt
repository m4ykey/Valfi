package com.m4ykey.valfi2.album.data.remote.model.album

data class AlbumsDto(
    val items: List<ItemDto>,
    val limit : Int,
    val next : String? = "",
    val offset : Int,
    val previous : String
)