package com.example.vuey.feature_music_player.presentation

import androidx.lifecycle.ViewModel
import com.example.vuey.feature_music_player.data.remote.api.YoutubeApi
import com.example.vuey.feature_music_player.data.remote.model.name.MusicItem
import com.example.vuey.feature_music_player.data.remote.model.name.MusicName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val youtubeApi: YoutubeApi
) : ViewModel() {

    private val _youtubeVideo = MutableStateFlow(MusicUiState())
    val youtubeVideo : StateFlow<MusicUiState> = _youtubeVideo

    suspend fun getMusicVideo(musicName : String) {

    }

}

data class MusicUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val musicData : MusicName? = null
)