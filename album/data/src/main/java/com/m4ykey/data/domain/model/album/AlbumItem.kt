package com.m4ykey.data.domain.model.album

data class AlbumItem(
    val artists : List<Artist>,
    val id : String,
    val images : List<Image>,
    val name : String
)
