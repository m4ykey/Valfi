package com.m4ykey.data.domain.model.track

import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.model.album.ExternalUrls

data class TrackItem(
    val artists: List<Artist>,
    val durationMs: Int,
    val explicit: Boolean,
    val externalUrls: ExternalUrls,
    val id: String,
    val name: String
)