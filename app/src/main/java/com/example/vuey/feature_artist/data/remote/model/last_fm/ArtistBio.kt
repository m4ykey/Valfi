package com.example.vuey.feature_artist.data.remote.model.last_fm

data class ArtistBio(
    val bio: Bio,
    val stats: Stats,
) {
    data class Stats(
        val listeners: String,
        val playcount: String
    )

    data class Bio(
        val content: String
    )
}