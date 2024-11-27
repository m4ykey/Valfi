package com.m4ykey.ui

import androidx.lifecycle.viewModelScope
import com.m4ykey.core.views.BaseViewModel
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.model.top_tracks.Track
import com.m4ykey.data.domain.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : BaseViewModel() {

    private var _artist = MutableStateFlow<Artist?>(null)
    val artist : StateFlow<Artist?> get() = _artist

    private var _topTracks = MutableStateFlow<List<Track>>(emptyList())
    val topTracks : StateFlow<List<Track>> get() = _topTracks

    fun getArtistInfo(id : String) {
        getArtistTopTracks(id)
        getArtist(id)
    }

    private fun getArtistTopTracks(id : String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                artistRepository.getArtistTopTracks(id).collect { tracks ->
                    _topTracks.value = tracks
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getArtist(artistId : String) = viewModelScope.launch {
        runFlow(
            flow = artistRepository.getArtist(artistId),
            resultState = _artist
        )
    }

}