package com.lyrics.ui

import androidx.lifecycle.viewModelScope
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import com.lyrics.data.domain.repository.LyricsRepository
import com.m4ykey.core.views.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val repository : LyricsRepository
) : BaseViewModel() {

    private var _lyrics = MutableStateFlow<LyricsItem?>(null)
    val lyrics : StateFlow<LyricsItem?> get() = _lyrics

    private var _track = MutableStateFlow<Track?>(null)
    val track : StateFlow<Track?> get() = _track

    fun getTrackById(trackId : String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                _error.value = null

                repository.getTrackById(trackId).collect { result ->
                    _track.value = result
                }
            } catch (e : Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchLyrics(artistName : String, trackName : String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                _error.value = null

                repository.searchLyrics(trackName = trackName, artistName = artistName).collect { result ->
                    _lyrics.value = result
                }
            } catch (e : Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

}