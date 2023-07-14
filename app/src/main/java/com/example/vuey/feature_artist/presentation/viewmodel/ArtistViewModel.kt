package com.example.vuey.feature_artist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_artist.domain.usecase.ArtistUseCase
import com.example.vuey.feature_artist.presentation.viewmodel.uistate.ArtistBioUiState
import com.example.vuey.feature_artist.presentation.viewmodel.uistate.ArtistInfoUiState
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val useCase : ArtistUseCase
) : ViewModel() {

    private val _artistBioUiState = MutableStateFlow(ArtistBioUiState())
    val artistBioUiState : StateFlow<ArtistBioUiState> = _artistBioUiState

    private val _artistInfoUiState = MutableStateFlow(ArtistInfoUiState())
    val artistInfoUiState : StateFlow<ArtistInfoUiState> = _artistInfoUiState

    suspend fun getArtistInfo(artistId : String) {
        useCase.getArtistInfoUseCase(artistId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _artistInfoUiState.value = artistInfoUiState.value.copy(
                        isLoading = false,
                        isError = result.message ?: "Unknown error",
                        artistInfoData = result.data
                    )
                }
                is Resource.Success -> {
                    _artistInfoUiState.value = artistInfoUiState.value.copy(
                        isLoading = false,
                        artistInfoData = result.data
                    )
                }
                is Resource.Loading -> {
                    _artistInfoUiState.value = artistInfoUiState.value.copy(
                        isLoading = true,
                        artistInfoData = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getArtistBio(artistName : String) {
        useCase.getArtistBioUseCase(artistName).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _artistBioUiState.value = artistBioUiState.value.copy(
                        isLoading = true,
                        artistBioData = result.data
                    )
                }
                is Resource.Success -> {
                    _artistBioUiState.value = artistBioUiState.value.copy(
                        isLoading = false,
                        artistBioData = result.data
                    )
                }
                is Resource.Failure -> {
                    _artistBioUiState.value = artistBioUiState.value.copy(
                        isLoading = false,
                        artistBioData = result.data,
                        isError = result.message ?: "Unknown error"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}