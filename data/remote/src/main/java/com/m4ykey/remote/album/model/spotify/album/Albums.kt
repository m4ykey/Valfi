package com.m4ykey.remote.album.model.spotify.album

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Albums(
    val items: List<AlbumList>
)