package com.example.vuey.feature_music_player.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_music_player.domain.use_case.YoutubeUseCase
import com.example.vuey.feature_music_player.presentation.viewmodel.uistate.YoutubeVideoNameUiState
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val useCase : YoutubeUseCase
) : ViewModel(){

    private val _getYoutubeVideoByNameUiState = MutableStateFlow(YoutubeVideoNameUiState())
    val getYoutubeVideoByNameUiState : StateFlow<YoutubeVideoNameUiState> = _getYoutubeVideoByNameUiState

    suspend fun getYoutubeVideo(videoName : String) {
        useCase.getYoutubeMusicNameUseCase(videoName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _getYoutubeVideoByNameUiState.value = getYoutubeVideoByNameUiState.value.copy(
                        isLoading = false,
                        youtubeVideoData = result.data ?: emptyList()
                    )
                }
                is Resource.Failure -> {
                    _getYoutubeVideoByNameUiState.value = getYoutubeVideoByNameUiState.value.copy(
                        isLoading = false,
                        youtubeVideoData = result.data ?: emptyList(),
                        isError = result.message ?: "Unknown error"
                    )
                }
                is Resource.Loading -> {
                    _getYoutubeVideoByNameUiState.value = getYoutubeVideoByNameUiState.value.copy(
                        isLoading = true,
                        youtubeVideoData = result.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}