package com.example.vuey.feature_artist.presentation.viewmodel.uistate

import com.example.vuey.feature_artist.data.remote.model.spotify.artist.ArtistInfo

data class ArtistInfoUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val artistInfoData : ArtistInfo? = null
)
