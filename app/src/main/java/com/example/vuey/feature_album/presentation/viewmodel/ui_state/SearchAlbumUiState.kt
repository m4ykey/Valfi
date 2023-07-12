package com.example.vuey.feature_album.presentation.viewmodel.ui_state

import com.example.vuey.feature_album.data.remote.model.spotify.album.Album

data class SearchAlbumUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val searchAlbumData : List<Album> = emptyList()
)