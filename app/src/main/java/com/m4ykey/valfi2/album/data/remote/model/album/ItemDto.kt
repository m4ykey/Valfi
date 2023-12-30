package com.m4ykey.valfi2.album.data.remote.model.album

data class ItemDto(
    val album_type: String,
    val artists: List<ArtistDto>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val images: List<ImageDto>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)