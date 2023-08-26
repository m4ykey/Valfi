package com.example.vuey.presentation.album.viewmodel.ui_state

import com.m4ykey.remote.album.model.spotify.album.AlbumList

sealed class SearchAlbumUiState {
    data object Loading : SearchAlbumUiState()
    data class Failure(val message : String) : SearchAlbumUiState()
    data class Success(val albumData : List<AlbumList>) : SearchAlbumUiState()
}