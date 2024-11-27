package com.m4ykey.data.remote.model.top_tracks

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.ArtistDto
import com.m4ykey.data.remote.model.ExternalUrlsDto
import com.m4ykey.data.remote.model.ImageDto

@Keep
data class AlbumDto(
    val album_type: String?,
    val artists: List<ArtistDto>?,
    val available_markets: List<String>?,
    val external_urls: ExternalUrlsDto?,
    val href: String?,
    val id: String?,
    val images: List<ImageDto>?,
    val is_playable: Boolean?,
    val name: String?,
    val release_date: String?,
    val release_date_precision: String?,
    val total_tracks: Int?,
    val type: String?,
    val uri: String?
)