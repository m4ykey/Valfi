package com.example.vuey.feature_album.presentation.viewmodel.ui_state

import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail

sealed class DetailAlbumUiState {
    data class Success(val albumData : AlbumDetail) : DetailAlbumUiState()
    data class Failure(val message : String) : DetailAlbumUiState()
    data object Loading : DetailAlbumUiState()
}