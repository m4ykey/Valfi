package com.m4ykey.data.remote.model.album

import androidx.annotation.Keep
import com.m4ykey.data.remote.model.artist.ArtistDto

@Keep
data class AlbumDetailDto(
    val album_type: String?,
    val artists: List<ArtistDto>?,
    val external_urls: ExternalUrlsDto?,
    val id: String?,
    val images: List<ImageDto>?,
    val label: String?,
    val name: String?,
    val release_date: String?,
    val total_tracks: Int?,
    val copyrights : List<CopyrightsDto>?
)
