package com.m4ykey.data.domain.model.track

import androidx.annotation.Keep
import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.model.album.ExternalUrls

@Keep
data class TrackItem(
    val artists: List<Artist>,
    val durationMs: Int,
    val explicit: Boolean,
    val externalUrls: ExternalUrls,
    val id: String,
    val name: String,
    val discNumber: Int
)