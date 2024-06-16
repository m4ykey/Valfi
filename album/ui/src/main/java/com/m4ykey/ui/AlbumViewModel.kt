package com.m4ykey.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    private val trackRepository: TrackRepository
) : ViewModel() {

    private var _search = MutableLiveData<List<AlbumItem>>()
    val albums : LiveData<List<AlbumItem>> get() = _search

    private var _newRelease = MutableLiveData<List<AlbumItem>>()
    val newRelease : LiveData<List<AlbumItem>> get() = _newRelease

    private var _detail = MutableLiveData<AlbumDetail>()
    val detail : LiveData<AlbumDetail> get() = _detail

    private var _tracks = MutableLiveData<List<TrackItem>>()
    val tracks : LiveData<List<TrackItem>> get() = _tracks

    private var _albumPaging = MutableLiveData<List<AlbumEntity>>()
    val albumPaging : LiveData<List<AlbumEntity>> get() = _albumPaging

    private var _searchResult = MutableLiveData<List<AlbumEntity>>()
    val searchResult : LiveData<List<AlbumEntity>> get() = _searchResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val _isLoadingTracks = MutableLiveData<Boolean>()
    val isLoadingTracks : LiveData<Boolean> get() = _isLoadingTracks

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> get() = _isError

    private var offset = 0
    var isPaginationEnded = false

    fun getAlbumDetails(id : String) {
        viewModelScope.launch {
            getAlbumTracks(id)
            getAlbumById(id)
        }
    }

    suspend fun getAlbumTracks(id : String) {
        if (_isLoadingTracks.value == true || isPaginationEnded) return

        _isLoadingTracks.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                trackRepository.getAlbumTracks(offset = offset, limit = PAGE_SIZE, id = id)
                    .collect { tracks ->
                        if (tracks.isNotEmpty()) {
                            val currentList = _tracks.value?.toMutableList() ?: mutableListOf()
                            currentList.addAll(tracks)
                            _tracks.value = currentList
                            offset += PAGE_SIZE
                            if (tracks.size < PAGE_SIZE) {
                                isPaginationEnded = true
                            }
                        } else {
                            isPaginationEnded = true
                        }
                    }
            } catch (e : Exception) {
                _isError.value = true
            } finally {
                _isLoadingTracks.value = false
            }
        }
    }

    private suspend fun getAlbumById(id : String) {
        _isLoading.value = true
        _isError.value = false

        viewModelScope.launch {
            try {
                repository.getAlbumById(id).collect { result ->
                    _detail.value = result
                }
            } catch (e : Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getNewReleases() {
        if (_isLoading.value == true || isPaginationEnded) return

        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                repository.getNewReleases(offset = offset, limit = PAGE_SIZE)
                    .collect { albums ->
                        if (albums.isNotEmpty()) {
                            val currentList = _newRelease.value?.toMutableList() ?: mutableListOf()
                            currentList.addAll(albums)
                            _newRelease.value = currentList
                            offset += PAGE_SIZE
                            if (albums.size < PAGE_SIZE) {
                                isPaginationEnded = true
                            }
                        } else {
                            isPaginationEnded = true
                        }
                    }
            } catch (e : Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun searchAlbums(query : String) {
        if (_isLoading.value == true || isPaginationEnded) return

        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                repository.searchAlbums(query, offset = offset, limit = PAGE_SIZE)
                    .collect { searchResult ->
                        if (searchResult.isNotEmpty()) {
                            val currentList = _search.value?.toMutableList() ?: mutableListOf()
                            currentList.addAll(searchResult)
                            _search.value = currentList
                            offset += PAGE_SIZE
                            if (searchResult.size < PAGE_SIZE) {
                                isPaginationEnded = true
                            }
                        } else {
                            isPaginationEnded = true
                        }
                    }
            } catch (e : Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSearch() {
        offset = 0
        _isLoading.value = false
        _isError.value = false
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

    fun getAlbumCount() : Flow<Int> = repository.getAlbumCount()

    fun getTotalTracksCount() : Flow<Int> = repository.getTotalTracksCount()

    fun getListenLaterCount() : Flow<Int> = repository.getListenLaterCount()

    fun getAlbumTypeCount(albumType : String) : Flow<Int> =repository.getAlbumTypeCount(albumType)

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