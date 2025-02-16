package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.UiState
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.repository.AlbumRepository
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithDetails
import com.m4ykey.data.local.model.DecadeResult
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.relations.AlbumWithStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private val _search = MutableStateFlow<UiState<List<AlbumItem>>>(UiState.Success(emptyList()))
    val search: StateFlow<UiState<List<AlbumItem>>> = _search.asStateFlow()

    private val _newRelease = MutableStateFlow<UiState<List<AlbumItem>>>(UiState.Success(emptyList()))
    val newRelease: StateFlow<UiState<List<AlbumItem>>> = _newRelease.asStateFlow()

    private val _detail = MutableStateFlow<UiState<AlbumDetail?>>(UiState.Success(null))
    val detail: StateFlow<UiState<AlbumDetail?>> = _detail.asStateFlow()

    private val _albumEntity = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val albumEntity: StateFlow<List<AlbumEntity>> = _albumEntity.asStateFlow()

    private val _searchResult = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val searchResult: StateFlow<List<AlbumEntity>> = _searchResult.asStateFlow()

    private val _albumCount = MutableStateFlow(0)
    val albumCount : StateFlow<Int> = _albumCount.asStateFlow()

    private val _totalTracksCount = MutableStateFlow(0)
    val totalTracksCount : StateFlow<Int> = _totalTracksCount.asStateFlow()

    private val _listenLaterCount = MutableStateFlow(0)
    val listenLaterCount : StateFlow<Int> = _listenLaterCount.asStateFlow()

    private val _randomAlbum = MutableStateFlow<AlbumEntity?>(null)
    val randomAlbum : StateFlow<AlbumEntity?> = _randomAlbum.asStateFlow()

    private val _decadeResult = MutableStateFlow<DecadeResult?>(null)
    val decadeResult : StateFlow<DecadeResult?> = _decadeResult.asStateFlow()

    private val _albumType = MutableStateFlow(0)
    val albumType : StateFlow<Int> = _albumType.asStateFlow()

    private val _epType = MutableStateFlow(0)
    val epType : StateFlow<Int> = _epType.asStateFlow()

    private val _singleType = MutableStateFlow(0)
    val singleType : StateFlow<Int> = _singleType.asStateFlow()

    private val _compilationType = MutableStateFlow(0)
    val compilationType : StateFlow<Int> = _compilationType.asStateFlow()

    private val _albumWithMostTracks = MutableStateFlow<AlbumWithDetails?>(null)
    val albumWithMostTracks : StateFlow<AlbumWithDetails?> = _albumWithMostTracks.asStateFlow()

    private var offset = 0
    private var isPaginationEnded = false

    fun getAlbumById(id: String) = viewModelScope.launch {
        _detail.value = UiState.Loading

        try {
            repository.getAlbumById(id).collect { result ->
                _detail.value = UiState.Success(result)
            }
        } catch (e: Exception) {
            _detail.value = UiState.Error(e)
        }
    }

    fun getNewReleases() {
        if (_newRelease.value is UiState.Loading || isPaginationEnded) return

        viewModelScope.launch {
            _newRelease.value = UiState.Loading

            try {
                repository.getNewReleases(offset, PAGE_SIZE).collect { albums ->
                    if (albums.isEmpty()) {
                        isPaginationEnded = true
                    } else {
                        val currentList = (_newRelease.value as? UiState.Success)?.data ?: emptyList()
                        val updatedList = currentList + albums
                        _newRelease.value = UiState.Success(updatedList)
                        offset += PAGE_SIZE
                        isPaginationEnded = albums.size < PAGE_SIZE
                    }
                }
            } catch (e: Exception) {
                _newRelease.value = UiState.Error(e)
            }
        }
    }

    fun searchAlbums(query: String) {
        if (_search.value is UiState.Loading || isPaginationEnded) return

        viewModelScope.launch {
            _search.value = UiState.Loading

            try {
                repository.searchAlbums(query = query, offset = offset, limit = PAGE_SIZE)
                    .collect { albums ->
                        if (albums.isEmpty()) {
                            isPaginationEnded = true
                        } else {
                            val currentList = (_search.value as? UiState.Success)?.data ?: emptyList()
                            val updatedList = currentList + albums
                            _search.value = UiState.Success(updatedList)
                            offset += PAGE_SIZE
                            isPaginationEnded = albums.size < PAGE_SIZE
                        }
                    }
            } catch (e: Exception) {
                _search.value = UiState.Error(e)
            }
        }
    }

    fun resetSearch() {
        offset = 0
        isPaginationEnded = false
        _search.value = UiState.Success(emptyList())
    }

    fun searchAlbumByName(albumName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.searchAlbumByName(albumName)
            loadAlbumsWithAdaptiveChunks(albums, _searchResult)
        }
    }

    fun searchAlbumsListenLater(albumName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.searchAlbumsListenLater(albumName)
            loadAlbumsWithAdaptiveChunks(albums, _searchResult)
        }
    }

    fun getAlbumType(albumType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getAlbumType(albumType)
            loadAlbumsWithAdaptiveChunks(albums, _albumEntity)
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
            loadAlbumsWithAdaptiveChunks(albums, _albumEntity)
        }
    }

    fun getSavedAlbumAsc() {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getSavedAlbumAsc()
            loadAlbumsWithAdaptiveChunks(albums, _albumEntity)
        }
    }

    fun getAlbumSortedByName() {
        viewModelScope.launch(Dispatchers.IO) {
            val albums = repository.getAlbumSortedByName()
            loadAlbumsWithAdaptiveChunks(albums, _albumEntity)
        }
    }

    fun getListenLaterAlbums() {
        viewModelScope.launch {
            val albums = repository.getListenLaterAlbums()
            loadAlbumsWithAdaptiveChunks(albums, _albumEntity)
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

    private suspend fun loadAlbumsWithAdaptiveChunks(
        albums : List<AlbumEntity>,
        stateFlow: MutableStateFlow<List<AlbumEntity>>
    ) {
        val displayedAlbums = mutableListOf<AlbumEntity>()

        val runtime = Runtime.getRuntime()
        val availableProcessors = runtime.availableProcessors()
        val freeMemory = runtime.freeMemory() / (1024 * 1024)

        val chunkSize = when {
            availableProcessors >= 8 && freeMemory > 4000 -> 20
            availableProcessors >= 4 && freeMemory > 2000 -> 15
            else -> 10
        }

        val delayTime = when {
            availableProcessors >= 8 && freeMemory > 4000 -> 500L
            availableProcessors >= 4 && freeMemory > 2000 -> 750L
            else -> 1000L
        }

        for (chunk in albums.chunked(chunkSize)) {
            displayedAlbums.addAll(chunk)
            stateFlow.emit(displayedAlbums.toList())
            delay(delayTime)
        }
    }
}