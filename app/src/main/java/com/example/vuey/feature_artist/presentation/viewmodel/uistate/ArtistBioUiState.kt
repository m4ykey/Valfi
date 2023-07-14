package com.example.vuey.feature_artist.presentation.viewmodel.uistate

import com.example.vuey.feature_artist.data.remote.model.last_fm.ArtistBio

data class ArtistBioUiState(
    val isLoading : Boolean = false,
    val isError : String ? = null,
    val artistBioData : ArtistBio? = null
)
