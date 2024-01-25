package com.m4ykey.ui.uistate

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.album.AlbumItem

data class AlbumSearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val albumSearch: PagingData<AlbumItem>? = null
)
