package com.m4ykey.ui.uistate

import com.m4ykey.data.domain.model.album.AlbumDetail

data class AlbumDetailUiState(
    val isLoading : Boolean = false,
    val error : String? = null,
    val albumDetail: AlbumDetail? = null
)
