package com.m4ykey.remote.album.model.spotify.album

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tracks(
    val items: List<AlbumItem>
) {
    @JsonClass(generateAdapter = true)
    data class AlbumItem(
        @field:Json(name = "artists")
        val artistList: List<Artist>,
        @field:Json(name = "duration_ms")
        val durationMs: Int,
        @field:Json(name = "external_urls")
        val externalUrls: ExternalUrls,
        val id: String,
        @field:Json(name = "name")
        val trackName: String,
        @field:Json(name = "type")
        val albumType: String,
    )
}