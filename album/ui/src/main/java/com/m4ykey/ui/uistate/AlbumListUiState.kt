package com.m4ykey.ui.uistate

import androidx.paging.PagingData
import com.m4ykey.data.domain.model.album.AlbumItem

data class AlbumListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val albumList: PagingData<AlbumItem>? = null
)
