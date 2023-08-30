package com.m4ykey.remote.album.model.spotify.album

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Artist(
    @field:Json(name = "external_urls")
    val externalUrls: ExternalUrls,
    val id: String,
    @field:Json(name = "name")
    val artistName: String,
) : Parcelable