package com.m4ykey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.views.BaseViewModel
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithDetails
import com.m4ykey.data.local.model.DecadeResult
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.helpers.PaginationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : BaseViewModel() {

    private val _search = MutableStateFlow<List<AlbumItem>>(emptyList())
    val search: StateFlow<List<AlbumItem>> get() = _search

    private val _newRelease = MutableStateFlow<List<AlbumItem>>(emptyList())
    val newRelease: StateFlow<List<AlbumItem>> get() = _newRelease

    private val _detail = MutableStateFlow<AlbumDetail?>(null)
    val detail: StateFlow<AlbumDetail?> get() = _detail

    private val _albumEntity = MutableLiveData<List<AlbumEntity>>()
    val albumEntity: LiveData<List<AlbumEntity>> get() = _albumEntity

    private val _searchResult = MutableLiveData<List<AlbumEntity>>()
    val searchResult: LiveData<List<AlbumEntity>> get() = _searchResult

    private val _albumCount = MutableStateFlow(0)
    val albumCount : StateFlow<Int> = _albumCount

    private val _totalTracksCount = MutableStateFlow(0)
    val totalTracksCount : StateFlow<Int> = _totalTracksCount

    private val _listenLaterCount = MutableStateFlow(0)
    val listenLaterCount : StateFlow<Int> = _listenLaterCount

    private val _randomAlbum = MutableStateFlow<AlbumEntity?>(null)
    val randomAlbum : StateFlow<AlbumEntity?> = _randomAlbum

    private val _decadeResult = MutableStateFlow<DecadeResult?>(null)
    val decadeResult : StateFlow<DecadeResult?> = _decadeResult

    private val _albumType = MutableStateFlow(0)
    val albumType : StateFlow<Int> = _albumType

    private val _epType = MutableStateFlow(0)
    val epType : StateFlow<Int> = _epType

    private val _singleType = MutableStateFlow(0)
    val singleType : StateFlow<Int> = _singleType

    private val _compilationType = MutableStateFlow(0)
    val compilationType : StateFlow<Int> = _compilationType

    private val _albumWithMostTracks = MutableStateFlow<AlbumWithDetails?>(null)
    val albumWithMostTracks : StateFlow<AlbumWithDetails?> = _albumWithMostTracks

    private var offset = 0

    fun loadNewDataIfNeeded(paginationType : PaginationType, query : String? = null) {
        if (!isPaginationEnded && !isLoading.value) {
            when (paginationType) {
                PaginationType.NEW_RELEASE -> getNewReleases()
                PaginationType.SEARCH -> searchAlbums(query ?: "")
            }
        }
    }

    fun getAlbumById(id: String) = viewModelScope.launch {
        _isLoading.value = true

        try {
            _error.value = null

            repository.getAlbumById(id).collect { result ->
                _detail.value = result
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "An unknown error occurred"
        } finally {
            _isLoading.value = false
        }
    }

    fun getNewReleases() {
        if (_isLoading.value || isPaginationEnded) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.getNewReleases(offset, PAGE_SIZE).collect { albums ->
                    if (albums.isEmpty()) {
                        isPaginationEnded = true
                    } else {
                        _newRelease.value += albums
                        offset += PAGE_SIZE
                        isPaginationEnded = albums.size < PAGE_SIZE
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchAlbums(query: String) {
        if (_isLoading.value || isPaginationEnded) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.searchAlbums(query = query, offset = offset, limit = PAGE_SIZE)
                    .collect { albums ->
                        if (albums.isEmpty()) {
                            isPaginationEnded = true
                        } else {
                            _search.value += albums
                            offset += PAGE_SIZE
                            isPaginationEnded = albums.size < PAGE_SIZE
                        }
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSearch() {
        offset = 0
        _isLoading.value = false
        _error.value = null
        isPaginationEnded = false
        _search.value = emptyList()
    }

    fun searchAlbumByName(albumName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.searchAlbumByName(albumName)
            _searchResult.postValue(albums)
        }
    }

    fun searchAlbumsListenLater(albumName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.searchAlbumsListenLater(albumName)
            _searchResult.postValue(albums)
        }
    }

    fun getAlbumType(albumType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getAlbumType(albumType)
            _albumEntity.postValue(albums)
        }
    }

    fun getAlbumWithMostTracks() {
        viewModelScope.launch {
            repository.getAlbumWithMostTracks().collect { album ->
                _albumWithMostTracks.value = album
            }
        }
    }

    fun getMostPopularDecade() {
        viewModelScope.launch {
            repository.getMostPopularDecade().collect { decade ->
                _decadeResult.value = decade
            }
        }
    }

    fun getListenLaterCount() {
        viewModelScope.launch {
            repository.getListenLaterCount().collect { count ->
                _listenLaterCount.value = count
            }
        }
    }

    fun getRandomAlbum() {
        viewModelScope.launch {
            repository.getRandomAlbum().collect { random ->
                _randomAlbum.value = random
            }
        }
    }

    fun getAlbumCount() {
        viewModelScope.launch {
            repository.getAlbumCount().collect { count ->
                _albumCount.value = count
            }
        }
    }

    fun getTotalTracksCount()  {
        viewModelScope.launch {
            repository.getTotalTracksCount().collect { count ->
                _totalTracksCount.value = count
            }
        }
    }

    fun getAlbumTypeCount(albumType: String) {
        viewModelScope.launch {
            val count = repository.getAlbumCountByType(albumType).firstOrNull() ?: 0
            when (albumType) {
                "Album" -> _albumType.value = count
                "Compilation" -> _compilationType.value = count
                "Single" -> _singleType.value = count
                "EP" -> _epType.value = count
            }
        }
    }

    fun getSavedAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getSavedAlbums()
            _albumEntity.postValue(albums)
        }
    }

    fun getSavedAlbumAsc() {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getSavedAlbumAsc()
            _albumEntity.postValue(albums)
        }
    }

    fun getAlbumSortedByName() {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getAlbumSortedByName()
            _albumEntity.postValue(albums)
        }
    }

    fun getListenLaterAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getListenLaterAlbums()
            _albumEntity.postValue(albums)
        }
    }

    suspend fun insertAlbum(album: AlbumEntity) = repository.insertAlbum(album)

    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) =
        repository.insertSavedAlbum(isAlbumSaved)

    suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved) =
        repository.insertListenLaterAlbum(isListenLaterSaved)

    suspend fun getAlbum(albumId: String): AlbumEntity? = repository.getAlbum(albumId)

    suspend fun getSavedAlbumState(albumId: String): IsAlbumSaved? =
        repository.getSavedAlbumState(albumId)

    suspend fun getListenLaterState(albumId: String): IsListenLaterSaved? =
        repository.getListenLaterState(albumId)

    suspend fun getAlbumWithStates(albumId: String): AlbumWithStates? =
        repository.getAlbumWithStates(albumId)

    suspend fun deleteAlbum(albumId: String) = repository.deleteAlbum(albumId)

    suspend fun deleteSavedAlbumState(albumId: String) = repository.deleteSavedAlbumState(albumId)

    suspend fun deleteListenLaterState(albumId: String) = repository.deleteListenLaterState(albumId)
}