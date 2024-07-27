package com.m4ykey.data.remote.model.tracks

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.album.ArtistDto
import com.m4ykey.data.remote.model.album.ExternalUrlsDto

@Keep
data class TrackItemDto(
    val artists: List<ArtistDto>?,
    val duration_ms: Int?,
    val explicit: Boolean?,
    val external_urls: ExternalUrlsDto?,
    val id: String?,
    val name: String?,
    val disc_number: Int?
)