package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.UiState
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.domain.usecase.track.GetLocalTrackUseCase
import com.m4ykey.data.domain.usecase.track.GetRemoteTrackUseCase
import com.m4ykey.data.local.model.TrackEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val getRemoteTrackUseCase: GetRemoteTrackUseCase,
    private val getLocalTrackUseCase : GetLocalTrackUseCase
) : ViewModel() {

    private var _totalTrackDurationMs = MutableStateFlow(0L)
    val totalTracksDuration = _totalTrackDurationMs.asStateFlow()

    private var _tracks = MutableStateFlow<UiState<List<TrackItem>>>(UiState.Success(emptyList()))
    val tracks = _tracks.asStateFlow()

    private var offset = 0
    var isPaginationEnded = false

    suspend fun getTracksById(albumId : String) : List<TrackEntity> = withContext(Dispatchers.IO) {
        getLocalTrackUseCase(GetLocalTrackUseCase.Params.GetTrack(albumId)) as List<TrackEntity>
    }

    suspend fun insertTracks(track : List<TrackEntity>) = withContext(Dispatchers.IO) {
        getLocalTrackUseCase(GetLocalTrackUseCase.Params.InsertTracks(track))
    }

    suspend fun deleteTracksById(albumId: String) = withContext(Dispatchers.IO) {
        getLocalTrackUseCase(GetLocalTrackUseCase.Params.DeleteTrack(albumId))
    }

    fun getAlbumTracks(id: String) {
        if (_tracks.value is UiState.Loading || isPaginationEnded) return

        viewModelScope.launch(Dispatchers.IO) {
            _tracks.value = UiState.Loading

            try {
                getRemoteTrackUseCase.getAlbumTracks(offset = offset, limit = PAGE_SIZE, id = id)
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