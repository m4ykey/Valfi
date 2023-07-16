package com.example.vuey.feature_artist.presentation.viewmodel.uistate

import com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks.Track

data class ArtistTopTracksUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val topTracksData : List<Track> = emptyList()
)
