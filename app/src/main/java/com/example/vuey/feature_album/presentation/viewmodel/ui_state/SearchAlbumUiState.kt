package com.example.vuey.feature_album.presentation.viewmodel.ui_state

import androidx.paging.PagingData
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumList
import kotlinx.coroutines.flow.Flow

sealed class SearchAlbumUiState {
    data object Loading : SearchAlbumUiState()
    data class Failure(val message : String) : SearchAlbumUiState()
    data class Success(val albumData : Flow<PagingData<AlbumList>>) : SearchAlbumUiState()
}