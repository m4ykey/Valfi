package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.ErrorState
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val trackRepository: TrackRepository
) : ViewModel() {

    private var _search = MutableStateFlow<List<AlbumItem>>(emptyList())
    val albums : StateFlow<List<AlbumItem>> get() = _search

    private var _newRelease = MutableStateFlow<List<AlbumItem>>(emptyList())
    val newRelease : StateFlow<List<AlbumItem>> get() = _newRelease

    private var _detail = MutableStateFlow<AlbumDetail?>(null)
    val detail : StateFlow<AlbumDetail?> get() = _detail

    private var _tracks = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracks : StateFlow<List<TrackItem>> get() = _tracks

    private var _albumPaging = MutableLiveData<List<AlbumEntity>>()
    val albumPaging : LiveData<List<AlbumEntity>> get() = _albumPaging

    private var _searchResult = MutableLiveData<List<AlbumEntity>>()
    val searchResult : LiveData<List<AlbumEntity>> get() = _searchResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    private val _isLoadingTracks = MutableStateFlow(false)
    val isLoadingTracks : StateFlow<Boolean> get() = _isLoadingTracks

    private val _isError = MutableStateFlow<ErrorState>(ErrorState.NoError)
    val isError : StateFlow<ErrorState> get() = _isError

    private var _totalTrackDurationMs = MutableStateFlow(0L)
    val totalTracksDuration : StateFlow<Long> = _totalTrackDurationMs.asStateFlow()

    private var offset = 0
    var isPaginationEnded = false

    fun getAlbumDetails(id : String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getAlbumTracks(id)
                getAlbumById(id)
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getAlbumTracks(id : String) = viewModelScope.launch {
        if (_isLoadingTracks.value || isPaginationEnded) return@launch

        _isLoadingTracks.value = true
        _isError.value = ErrorState.NoError
        try {
            trackRepository.getAlbumTracks(offset = offset, limit = PAGE_SIZE, id = id)
                .collect { tracks ->
                    if (tracks.isNotEmpty()) {
                        val currentList = _tracks.value.toMutableList()
                        currentList.addAll(tracks)
                        _tracks.value = currentList

                        val totalDuration = tracks.sumOf { it.durationMs.toLong() }
                        _totalTrackDurationMs.value += totalDuration

                        offset += PAGE_SIZE
                        if (tracks.size < PAGE_SIZE) isPaginationEnded = true
                    } else {
                        isPaginationEnded = true
                    }
                }
        } catch (e : Exception) {
            _isError.value = ErrorState.Error(e.message ?: "Unknown error")
        } finally {
            _isLoadingTracks.value = false
        }
    }

    private suspend fun getAlbumById(id : String) = viewModelScope.launch {
        _isLoading.value = true
        _isError.value = ErrorState.NoError

        try {
            repository.getAlbumById(id).collect { result ->
                _detail.value = result
            }
        } catch (e : Exception) {
            _isError.value = ErrorState.Error(e.message ?: "Unknown error")
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun getNewReleases() = viewModelScope.launch {
        if (_isLoading.value || isPaginationEnded) return@launch

        _isLoading.value = true
        _isError.value = ErrorState.NoError
        try {
            repository.getNewReleases(offset = offset, limit = PAGE_SIZE)
                .collect { albums ->
                    if (albums.isNotEmpty()) {
                        val currentList = _newRelease.value.toMutableList()
                        currentList.addAll(albums)
                        _newRelease.value = currentList
                        offset += PAGE_SIZE
                        if (albums.size < PAGE_SIZE) isPaginationEnded = true
                    } else {
                        isPaginationEnded = true
                    }
                }
        } catch (e : Exception) {
            _isError.value = ErrorState.Error(e.message ?: "Unknown error")
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun searchAlbums(query : String) = viewModelScope.launch {
        if (_isLoading.value || isPaginationEnded) return@launch

        _isLoading.value = true
        _isError.value = ErrorState.NoError
        try {
            repository.searchAlbums(query, offset = offset, limit = PAGE_SIZE)
                .collect { searchResult ->
                    if (searchResult.isNotEmpty()) {
                        val currentList = _search.value.toMutableList()
                        currentList.addAll(searchResult)
                        _search.value = currentList
                        offset += PAGE_SIZE
                        if (searchResult.size < PAGE_SIZE) isPaginationEnded = true
                    } else {
                        isPaginationEnded = true
                    }
                }
        } catch (e : Exception) {
            _isError.value = ErrorState.Error(e.message ?: "Unknown error")
        } finally {
            _isLoading.value = false
        }
    }

    fun resetSearch() {
        offset = 0
        _isLoading.value = false
        _isError.value = ErrorState.NoError
        isPaginationEnded = false
        _search.value = emptyList()
    }

    suspend fun searchAlbumByName(albumName : String) {
        viewModelScope.launch {
            val result = repository.searchAlbumByName(albumName)
            _searchResult.value = result
        }
    }

    suspend fun searchAlbumsListenLater(albumName : String) {
        viewModelScope.launch {
            val result = repository.searchAlbumsListenLater(albumName)
            _searchResult.value = result
        }
    }

    suspend fun getAlbumType(albumType : String) {
        viewModelScope.launch {
            val result = repository.getAlbumType(albumType)
            _albumPaging.value = result
        }
    }

    fun getListenLaterCount() : Flow<Int> = repository.getListenLaterCount()

    suspend fun getRandomAlbum() : AlbumEntity? = repository.getRandomAlbum()

    suspend fun getSavedAlbums() {
        viewModelScope.launch {
            val result = repository.getSavedAlbums()
            _albumPaging.value = result
        }
    }

    suspend fun getSavedAlbumAsc() {
        viewModelScope.launch {
            val result = repository.getSavedAlbumAsc()
            _albumPaging.value = result
        }
    }

    suspend fun getAlbumSortedByName() {
        viewModelScope.launch {
            val result = repository.getAlbumSortedByName()
            _albumPaging.value = result
        }
    }

    suspend fun getListenLaterAlbums() {
        viewModelScope.launch {
            val result = repository.getListenLaterAlbums()
            _albumPaging.value = result
        }
    }

    suspend fun insertAlbum(album : AlbumEntity) = repository.insertAlbum(album)

    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) = repository.insertSavedAlbum(isAlbumSaved)

    suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved) = repository.insertListenLaterAlbum(isListenLaterSaved)

    suspend fun getAlbum(albumId : String) : AlbumEntity? = repository.getAlbum(albumId)

    suspend fun getSavedAlbumState(albumId : String) : IsAlbumSaved? = repository.getSavedAlbumState(albumId)

    suspend fun getListenLaterState(albumId: String) : IsListenLaterSaved? = repository.getListenLaterState(albumId)

    suspend fun getAlbumWithStates(albumId : String) : AlbumWithStates? = repository.getAlbumWithStates(albumId)

    suspend fun deleteAlbum(albumId: String) = repository.deleteAlbum(albumId)

    suspend fun deleteSavedAlbumState(albumId : String) = repository.deleteSavedAlbumState(albumId)

    suspend fun deleteListenLaterState(albumId: String) = repository.deleteListenLaterState(albumId)
}