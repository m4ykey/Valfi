package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.UiState
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val repository : TrackRepository
) : ViewModel() {

    private var _totalTrackDurationMs = MutableStateFlow(0L)
    val totalTracksDuration: StateFlow<Long> = _totalTrackDurationMs

    private var _tracks = MutableStateFlow<UiState<List<TrackItem>>>(UiState.Success(emptyList()))
    val tracks: StateFlow<UiState<List<TrackItem>>> = _tracks.asStateFlow()

    private var offset = 0
    var isPaginationEnded = false

    fun getAlbumTracks(id: String) {
        if (_tracks.value is UiState.Loading || isPaginationEnded) return

        viewModelScope.launch {
            _tracks.value = UiState.Loading

            try {
                repository.getAlbumTracks(offset = offset, limit = PAGE_SIZE, id = id)
                    .collect { tracks ->
                        if (tracks.isEmpty()) {
                            isPaginationEnded = true
                        } else {
                            val currentList = (_tracks.value as? UiState.Success)?.data ?: emptyList()
                            val updatedList = currentList + tracks
                            _tracks.value = UiState.Success(updatedList)
                            offset += PAGE_SIZE
                            isPaginationEnded = tracks.size < PAGE_SIZE

                            val totalDuration = tracks.sumOf { it.durationMs.toLong() }
                            _totalTrackDurationMs.value += totalDuration
                        }
                    }
            } catch (e: Exception) {
                _tracks.value = UiState.Error(e)
            }
        }
    }
}