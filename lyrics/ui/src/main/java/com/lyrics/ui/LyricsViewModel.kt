package com.lyrics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import com.lyrics.data.domain.usecase.LyricsUseCase
import com.m4ykey.core.network.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val useCase : LyricsUseCase
) : ViewModel() {

    private var _lyrics = MutableStateFlow<UiState<LyricsItem?>>(UiState.Success(null))
    val lyrics = _lyrics.asStateFlow()

    private var _track = MutableStateFlow<UiState<Track?>>(UiState.Success(null))
    val track = _track.asStateFlow()

    fun getTrackById(trackId : String) = viewModelScope.launch {
        _track.value = UiState.Loading

        useCase.getTrackById(trackId)
            .catch { e -> _track.value = UiState.Error(e) }
            .collect { result -> _track.value = UiState.Success(result) }
    }

    fun searchLyrics(artistName : String, trackName : String) = viewModelScope.launch {
        _lyrics.value = UiState.Loading

        useCase.searchLyrics(name = trackName, artist = artistName)
            .catch { e -> _lyrics.value = UiState.Error(e) }
            .collect { result -> _lyrics.value = UiState.Success(result) }
    }
}