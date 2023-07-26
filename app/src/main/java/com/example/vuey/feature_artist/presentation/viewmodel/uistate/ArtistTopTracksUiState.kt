package com.example.vuey.feature_artist.presentation.viewmodel.uistate

import com.example.vuey.feature_artist.data.remote.model.spotify.top_tracks.Track

sealed class ArtistTopTracksUiState {
    data object Loading : ArtistTopTracksUiState()
    data class Failure(val message : String) : ArtistTopTracksUiState()
    data class Success(val topTracksData : List<Track>) : ArtistTopTracksUiState()
}
