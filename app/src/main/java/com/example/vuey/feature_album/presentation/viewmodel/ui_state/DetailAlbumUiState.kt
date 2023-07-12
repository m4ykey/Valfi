package com.example.vuey.feature_album.presentation.viewmodel.ui_state

import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumDetail

data class DetailAlbumUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val detailAlbumData : AlbumDetail? = null
)