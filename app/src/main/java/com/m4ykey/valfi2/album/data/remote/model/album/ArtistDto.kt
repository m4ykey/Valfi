package com.m4ykey.valfi2.album.data.remote.model.album

data class ArtistDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)