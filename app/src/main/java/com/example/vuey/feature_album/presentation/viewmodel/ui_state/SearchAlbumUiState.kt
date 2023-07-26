package com.example.vuey.feature_album.presentation.viewmodel.ui_state

import com.example.vuey.feature_album.data.remote.model.spotify.album.Album

sealed class SearchAlbumUiState {
    object Loading : SearchAlbumUiState()
    data class Failure(val message : String) : SearchAlbumUiState()
    data class Success(val albumData : List<Album>) : SearchAlbumUiState()

    val isLoading : Boolean
        get() = this is Loading
}