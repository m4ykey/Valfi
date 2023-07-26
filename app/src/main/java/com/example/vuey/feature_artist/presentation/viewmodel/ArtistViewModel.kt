package com.example.vuey.feature_artist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.core.common.network.Resource
import com.example.vuey.feature_artist.domain.repository.ArtistRepository
import com.example.vuey.feature_artist.presentation.viewmodel.uistate.ArtistInfoUiState
import com.example.vuey.feature_artist.presentation.viewmodel.uistate.ArtistTopTracksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository : ArtistRepository
) : ViewModel() {

    private val _artistInfoUiState = MutableStateFlow<ArtistInfoUiState>(ArtistInfoUiState.Loading)
    val artistInfoUiState : StateFlow<ArtistInfoUiState> = _artistInfoUiState

    private val _artistTopTracksUiState = MutableStateFlow<ArtistTopTracksUiState>(ArtistTopTracksUiState.Loading)
    val artistTopTracksUiState : StateFlow<ArtistTopTracksUiState> = _artistTopTracksUiState

    suspend fun getArtistTopTracks(artistId: String) {
        repository.getArtistTopTracks(artistId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _artistTopTracksUiState.tryEmit(
                        ArtistTopTracksUiState.Failure(
                            message = result.message ?: "Unknown error"
                        )
                    )
                }

                is Resource.Success -> {
                    _artistTopTracksUiState.tryEmit(
                        ArtistTopTracksUiState.Success(
                            topTracksData = result.data ?: emptyList()
                        )
                    )
                }

                is Resource.Loading -> {
                    _artistTopTracksUiState.tryEmit(ArtistTopTracksUiState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getArtistInfo(artistId : String) {
        repository.getArtistInfo(artistId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _artistInfoUiState.tryEmit(
                        ArtistInfoUiState.Failure(
                            message = result.message ?: "Unknown error"
                        )
                    )
                }
                is Resource.Success -> {
                    _artistInfoUiState.tryEmit(
                        ArtistInfoUiState.Success(
                            artistData = result.data!!
                        )
                    )
                }
                is Resource.Loading -> {
                    _artistInfoUiState.tryEmit(ArtistInfoUiState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }

}