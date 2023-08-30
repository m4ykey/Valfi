package com.m4ykey.remote.album.model.spotify.album

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class AlbumList(
    @field:Json(name = "album_type")
    val albumType: String,
    @field:Json(name = "artists")
    val artistList: List<Artist>,
    @field:Json(name = "external_urls")
    val externalUrls: ExternalUrls?,
    val id: String,
    @field:Json(name = "images")
    val imageList: List<Image>,
    @field:Json(name = "name")
    val albumName: String,
    @field:Json(name = "total_tracks")
    val totalTracks: Int
) : Parcelable