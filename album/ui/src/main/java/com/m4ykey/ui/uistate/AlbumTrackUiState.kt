package com.m4ykey.ui.uistate

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.track.TrackItem

data class AlbumTrackUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val albumTracks : PagingData<TrackItem>? = null
)
