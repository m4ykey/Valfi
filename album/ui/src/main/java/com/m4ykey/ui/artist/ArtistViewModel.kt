package com.m4ykey.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.network.UiState
import com.m4ykey.data.domain.model.artist.Artist
import com.m4ykey.data.domain.model.artist.ArtistList
import com.m4ykey.data.domain.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository: ArtistRepository
) : ViewModel() {

    private val _artists = MutableStateFlow<UiState<List<ArtistList>>>(UiState.Success(emptyList()))
    val artists = _artists.asStateFlow()

    fun loadArtists(id : String) = viewModelScope.launch {
        _artists.value = UiState.Loading

        repository.getSeveralArtists(ids = id)
            .catch { e -> _artists.value = UiState.Error(e) }
            .collect { result -> _artists.value = UiState.Success(result) }
    }
}