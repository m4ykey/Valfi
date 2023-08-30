package com.m4ykey.remote.album.model.spotify.album

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumDetail(
    @field:Json(name = "album_type")
    val albumType: String,
    @field:Json(name = "artists")
    val artistList: List<Artist>,
    @field:Json(name = "external_urls")
    val externalUrls: ExternalUrls,
    val id: String,
    @field:Json(name = "images")
    val imageList: List<Image>,
    @field:Json(name = "name")
    val albumName: String,
    @field:Json(name = "release_date")
    val releaseDate: String,
    @field:Json(name = "total_tracks")
    val totalTracks: Int,
    val tracks: Tracks
)