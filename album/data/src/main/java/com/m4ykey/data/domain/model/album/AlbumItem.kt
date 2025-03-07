package com.m4ykey.data.domain.model.album

import com.m4ykey.data.domain.model.artist.Artist

data class AlbumItem(
    val artists : List<Artist>,
    val id : String,
    val images : List<Image>,
    val name : String,
    val albumType : String
)
