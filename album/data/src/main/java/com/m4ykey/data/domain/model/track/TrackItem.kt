package com.m4ykey.data.domain.model.track

import com.m4ykey.data.domain.model.album.ExternalUrls
import com.m4ykey.data.domain.model.artist.Artist
import java.util.UUID

data class TrackItem(
    val artists: List<Artist>,
    val durationMs: Int,
    val explicit: Boolean,
    val externalUrls: ExternalUrls,
    val id: String,
    val name: String,
    val discNumber: Int
) {
    val longId : Long
        get() = UUID.nameUUIDFromBytes(id.toByteArray()).mostSignificantBits
}