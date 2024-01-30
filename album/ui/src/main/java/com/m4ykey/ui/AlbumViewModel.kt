package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.core.network.Resource
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumSearchUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private var _albums = MutableLiveData<AlbumSearchUiState>()
    val albums : LiveData<AlbumSearchUiState> get() = _albums

    private var _detail = MutableLiveData<AlbumDetailUiState>()
    val detail : LiveData<AlbumDetailUiState> get() = _detail

    private var _tracks = MutableLiveData<AlbumTrackUiState>()
    val tracks : LiveData<AlbumTrackUiState> get() = _tracks

    private var _albumPagingData = MutableStateFlow<PagingData<AlbumEntity>>(PagingData.empty())
    val albumPagingData: StateFlow<PagingData<AlbumEntity>> = _albumPagingData

    init {
        getAllAlbumsPaged()
    }

    private fun getAllAlbumsPaged() {
        viewModelScope.launch {
            repository.getAllAlbumsPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.value = pagingData
            }
        }
    }

    suspend fun insertAlbum(album : AlbumEntity) {
        viewModelScope.launch { repository.insertAlbum(album) }
    }

    suspend fun deleteAlbum(album : AlbumEntity) {
        viewModelScope.launch { repository.deleteAlbum(album) }
    }

    suspend fun getAlbumById(id : String) {
        repository.getAlbumById(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _detail.value = AlbumDetailUiState(
                        isLoading = true,
                        albumDetail = null,
                        error = null
                    )
                }
                is Resource.Success -> {
                    _detail.value = AlbumDetailUiState(
                        isLoading = false,
                        albumDetail = result.data,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _detail.value = AlbumDetailUiState(
                        isLoading = false,
                        error = result.message ?: "Unknown error",
                        albumDetail = null
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun searchAlbums(query : String) {
        viewModelScope.launch {
            _albums.value = AlbumSearchUiState(isLoading = true)
            try {
                repository.searchAlbums(query).cachedIn(viewModelScope).collectLatest { albums ->
                    _albums.value = AlbumSearchUiState(albumSearch = albums)
                }
            } catch (e : Exception) {
                _albums.value = AlbumSearchUiState(error = e.message ?: "Unknown error")
            } finally {
                _albums.value = AlbumSearchUiState(isLoading = false)
            }
        }
    }

    fun getAlbumTracks(id : String) {
        viewModelScope.launch {
            _tracks.value = AlbumTrackUiState(isLoading = true)
            try {
                repository.getAlbumTracks(id).cachedIn(viewModelScope).collectLatest { tracks ->
                    _tracks.value = AlbumTrackUiState(albumTracks = tracks)
                }
            } catch (e : Exception) {
                _tracks.value = AlbumTrackUiState(error = e.message ?: "Unknown error")
            } finally {
                _tracks.value = AlbumTrackUiState(isLoading = false)
            }
        }
    }
}