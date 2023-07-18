package com.example.vuey.feature_album.presentation.viewmodel.ui_state

data class YoutubeUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val youtubeData : List<String> = emptyList()
)
