package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.core.network.Resource
import com.m4ykey.core.network.handleResult
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.ui.helpers.ListSortingType
import com.m4ykey.ui.helpers.ViewType
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumListUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private var _search = MutableLiveData<AlbumListUiState>()
    val albums : LiveData<AlbumListUiState> get() = _search

    private var _newRelease = MutableLiveData<AlbumListUiState>()
    val newRelease : LiveData<AlbumListUiState> get() = _newRelease

    private var _detail = MutableLiveData<AlbumDetailUiState>()
    val detail : LiveData<AlbumDetailUiState> get() = _detail

    private var _tracks = MutableLiveData<AlbumTrackUiState>()
    val tracks : LiveData<AlbumTrackUiState> get() = _tracks

    private var _currentViewType = MutableLiveData(ViewType.GRID)
    val currentViewType : LiveData<ViewType> = _currentViewType

    private var currentSortingType : ListSortingType = ListSortingType.RECENTLY_ADDED

    init {
        getNewReleases()
    }

    suspend fun getAlbumById(id : String) {
        repository.getAlbumById(id).onEach { result ->
            _detail.value = handleDetailState(result)
        }.launchIn(viewModelScope)
    }

    private fun getNewReleases() {
        viewModelScope.launch {
            _newRelease.value = AlbumListUiState(isLoading = true)
            try {
                val result = repository.getNewReleases()
                    .cachedIn(viewModelScope)
                    .map { Resource.Success(it) }
                handleNewReleaseResult(result)
            } finally {
                _newRelease.value = _newRelease.value?.copy(isLoading = false)
            }
        }
    }

    fun searchAlbums(query : String) {
        viewModelScope.launch {
            _search.value = AlbumListUiState(isLoading = true)
            try {
                val result = repository.searchAlbums(query)
                    .cachedIn(viewModelScope)
                    .map { Resource.Success(it) }
                handleSearchResult(result)
            } finally {
                _search.value = _search.value?.copy(isLoading = false)
            }
        }
    }

    fun getAlbumTracks(id : String) {
        viewModelScope.launch {
            _tracks.value = AlbumTrackUiState(isLoading = true)
            try {
                val result = repository.getAlbumTracks(id)
                    .cachedIn(viewModelScope)
                    .map { Resource.Success(it) }
                handleTrackResult(result)
            } finally {
                _tracks.value = _tracks.value?.copy(isLoading = false)
            }
        }
    }

    private fun handleDetailState(result : Resource<AlbumDetail>) : AlbumDetailUiState {
        return when (result) {
            is Resource.Success -> AlbumDetailUiState(albumDetail = result.data)
            is Resource.Error -> AlbumDetailUiState(error = result.message)
            is Resource.Loading -> AlbumDetailUiState(isLoading = true)
        }
    }

    private fun handleSearchError(e : Exception) {
        _search.value = AlbumListUiState(error = e.message)
    }

    private fun handleNewReleaseError(e : Exception) {
        _newRelease.value = AlbumListUiState(error = e.message)
    }

    private fun handleTrackError(e : Exception) {
        _tracks.value = AlbumTrackUiState(error = e.message)
    }

    private fun handleSearchResult(result : Flow<Resource<PagingData<AlbumItem>>>) {
        viewModelScope.launch {
            result.collect { resource ->
                resource.handleResult(
                    onSuccess = { albums ->
                        _search.value = AlbumListUiState(albumList = albums)
                    },
                    onError = { e -> handleSearchError(e) }
                )
            }
        }
    }

    private fun handleTrackResult(result : Flow<Resource<PagingData<TrackItem>>>) {
        viewModelScope.launch {
            result.collect { resource ->
                resource.handleResult(
                    onSuccess = { tracks ->
                        _tracks.value = AlbumTrackUiState(albumTracks = tracks)
                    },
                    onError = { e -> handleTrackError(e)}
                )
            }
        }
    }

    private fun handleNewReleaseResult(result : Flow<Resource<PagingData<AlbumItem>>>) {
        viewModelScope.launch {
            result.collect { resource ->
                resource.handleResult(
                    onError = { e -> handleNewReleaseError(e) },
                    onSuccess = { albums ->
                        _newRelease.value = AlbumListUiState(albumList = albums)
                    }
                )
            }
        }
    }
}