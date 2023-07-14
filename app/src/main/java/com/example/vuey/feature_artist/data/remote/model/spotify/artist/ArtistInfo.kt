package com.example.vuey.feature_artist.data.remote.model.spotify.artist

data class ArtistInfo(
    val external_urls: ExternalUrls,
    val followers: Followers,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int
)