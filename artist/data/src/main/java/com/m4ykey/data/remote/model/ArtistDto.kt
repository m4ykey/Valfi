package com.m4ykey.data.remote.model

import androidx.annotation.Keep

@Keep
data class ArtistDto(
    val external_urls: ExternalUrlsDto?,
    val followers: FollowersDto?,
    val genres: List<String?>?,
    val href: String?,
    val id: String?,
    val images: List<ImageDto?>?,
    val name: String?,
    val popularity: Int?,
    val type: String?,
    val uri: String?
)