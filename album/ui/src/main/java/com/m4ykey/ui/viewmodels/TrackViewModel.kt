package com.m4ykey.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.views.BaseViewModel
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val repository : TrackRepository
) : BaseViewModel() {

    private var _totalTrackDurationMs = MutableStateFlow(0L)
    val totalTracksDuration: StateFlow<Long> = _totalTrackDurationMs

    private var _tracks = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracks: StateFlow<List<TrackItem>> get() = _tracks

    private var offset = 0

    fun getAlbumTracks(id: String) {
        if (_isLoading.value || isPaginationEnded) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.getAlbumTracks(offset = offset, limit = PAGE_SIZE, id = id)
                    .collect { tracks ->
                        if (tracks.isEmpty()) {
                            isPaginationEnded = true
                        } else {
                            _tracks.value += tracks
                            offset += PAGE_SIZE
                            isPaginationEnded = tracks.size < PAGE_SIZE

                            val totalDuration = tracks.sumOf { it.durationMs.toLong() }
                            _totalTrackDurationMs.value += totalDuration
                        }
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

}