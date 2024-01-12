package com.m4ykey.data.domain.model

data class Albums(
    val items : List<AlbumItem>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String
)
