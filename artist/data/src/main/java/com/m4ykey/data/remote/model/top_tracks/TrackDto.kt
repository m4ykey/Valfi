package com.m4ykey.data.remote.model.top_tracks

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.ArtistDto
import com.m4ykey.data.remote.model.ExternalUrlsDto

@Keep
data class TrackDto(
    val album: AlbumDto?,
    val artists: List<ArtistDto>?,
    val available_markets: List<String>?,
    val disc_number: Int?,
    val duration_ms: Int?,
    val explicit: Boolean?,
    val external_urls: ExternalUrlsDto?,
    val href: String?,
    val id: String?,
    val is_local: Boolean?,
    val is_playable: Boolean?,
    val name: String?,
    val popularity: Int?,
    val preview_url: Any?,
    val track_number: Int?,
    val type: String?,
    val uri: String?
)