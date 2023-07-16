package com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks

import com.example.vuey.feature_album.data.remote.model.spotify.album.Artist

data class Track(
    val album: Album,
    val artists: List<Artist>,
    val id: String,
    val name: String,
)