package com.example.vuey.presentation.album.viewmodel.ui_state

import com.m4ykey.remote.album.model.spotify.album.AlbumDetail

sealed class DetailAlbumUiState {
    data class Success(val albumData : AlbumDetail) : DetailAlbumUiState()
    data class Failure(val message : String) : DetailAlbumUiState()
    data object Loading : DetailAlbumUiState()
}