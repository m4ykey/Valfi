package com.lyrics.data.remote.model

import androidx.annotation.Keep

@Keep
data class TrackDto(
    val album: AlbumDto?,
    val external_urls: ExternalUrlsDto?,
    val id: String?
)