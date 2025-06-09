package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.UiState
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.usecase.track.GetLocalTrackUseCase
import com.m4ykey.data.domain.usecase.track.GetRemoteTrackUseCase
import com.m4ykey.data.local.model.TrackEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val getRemoteTrackUseCase: GetRemoteTrackUseCase,
    private val getLocalTrackUseCase : GetLocalTrackUseCase,
    private val dispatcherIO : CoroutineDispatcher
) : ViewModel() {

    private var _totalTrackDurationMs = MutableStateFlow(0L)
    val totalTracksDuration = _totalTrackDurationMs.asStateFlow()

    private val _tracks = MutableStateFlow<UiState<PagingData<TrackItem>>>(UiState.Success(PagingData.empty()))
    val tracks = _tracks.asStateFlow()

    var isPaginationEnded = false

    suspend fun getTracksById(albumId : String) : List<TrackEntity> = withContext(dispatcherIO) {
        getLocalTrackUseCase(GetLocalTrackUseCase.Params.GetTrack(albumId)) as List<TrackEntity>
    }

    suspend fun insertTracks(track : List<TrackEntity>) = withContext(dispatcherIO) {
        getLocalTrackUseCase(GetLocalTrackUseCase.Params.InsertTracks(track))
    }

    suspend fun deleteTracksById(albumId: String) = withContext(dispatcherIO) {
        getLocalTrackUseCase(GetLocalTrackUseCase.Params.DeleteTrack(albumId))
    }

    fun getAlbumTracks(id: String) {
        _tracks.value = UiState.Loading

        viewModelScope.launch {
            try {
                delay(1000L)

                getRemoteTrackUseCase.getAlbumTracks(id = id)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _tracks.value = UiState.Success(pagingData)
                    }
            } catch (e : Exception) {
                _tracks.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}