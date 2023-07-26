package com.example.vuey.feature_artist.presentation.viewmodel.uistate

import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo

sealed class ArtistInfoUiState {
    data object Loading : ArtistInfoUiState()
    data class Failure(val message : String) : ArtistInfoUiState()
    data class Success(val artistData : ArtistInfo) : ArtistInfoUiState()
}