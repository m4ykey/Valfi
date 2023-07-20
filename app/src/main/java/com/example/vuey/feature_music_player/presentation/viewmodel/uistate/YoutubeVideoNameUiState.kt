package com.example.vuey.feature_music_player.presentation.viewmodel.uistate

import com.example.vuey.feature_music_player.data.remote.model.name.YoutubeSearchItems

data class YoutubeVideoNameUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val youtubeVideoData : List<YoutubeSearchItems> = emptyList()
)
