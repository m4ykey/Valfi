package com.m4ykey.ui.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.Constants.PAGE_SIZE
import com.m4ykey.core.network.UiState
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.usecase.album.DeleteAlbumUseCase
import com.m4ykey.data.domain.usecase.album.GetAlbumUseCase
import com.m4ykey.data.domain.usecase.album.GetLocalAlbumUseCase
import com.m4ykey.data.domain.usecase.album.GetRemoteAlbumUseCase
import com.m4ykey.data.domain.usecase.album.SaveAlbumUseCase
import com.m4ykey.data.domain.usecase.album.StatisticsAlbumUseCase
import com.m4ykey.data.domain.usecase.stars.GetStarsUseCase
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.AlbumWithDetails
import com.m4ykey.data.local.model.DecadeResult
import com.m4ykey.data.local.model.IsAlbumSaved
import com.m4ykey.data.local.model.IsListenLaterSaved
import com.m4ykey.data.local.model.StarsEntity
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.album.helpers.CacheConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val deleteAlbumUseCase: DeleteAlbumUseCase,
    private val getLocalAlbumUseCase: GetLocalAlbumUseCase,
    private val getRemoteAlbumUseCase : GetRemoteAlbumUseCase,
    private val saveAlbumUseCase : SaveAlbumUseCase,
    private val getAlbumUseCase : GetAlbumUseCase,
    private val statisticsAlbumUseCase: StatisticsAlbumUseCase,
    private val getStarsUseCase : GetStarsUseCase,
    private val dispatcherIO : CoroutineDispatcher
) : ViewModel() {

    private val _search = MutableStateFlow<UiState<List<AlbumItem>>>(UiState.Success(emptyList()))
    val search = _search.asStateFlow()

    private val _newRelease = MutableStateFlow<UiState<List<AlbumItem>>>(UiState.Success(emptyList()))
    val newRelease = _newRelease.asStateFlow()

    private val _detail = MutableStateFlow<UiState<AlbumDetail?>>(UiState.Success(null))
    val detail = _detail.asStateFlow()

    private val _albumEntity = MutableSharedFlow<List<AlbumEntity>>(replay = 1)
    val albumEntity = _albumEntity.asSharedFlow()

    private val _searchResult = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    private val _albumCount = MutableStateFlow(0)
    val albumCount = _albumCount.asStateFlow()

    private val _totalTracksCount = MutableStateFlow(0)
    val totalTracksCount = _totalTracksCount.asStateFlow()

    private val _listenLaterCount = MutableStateFlow(0)
    val listenLaterCount = _listenLaterCount.asStateFlow()

    private val _randomAlbum = MutableStateFlow<AlbumEntity?>(null)
    val randomAlbum = _randomAlbum.asStateFlow()

    private val _decadeResult = MutableStateFlow<DecadeResult?>(null)
    val decadeResult = _decadeResult.asStateFlow()

    private val _albumWithMostTracks = MutableStateFlow<AlbumWithDetails?>(null)
    val albumWithMostTracks = _albumWithMostTracks.asStateFlow()

    var offset = 0
    var isPaginationEnded = false

    private val _albumTypes = MutableStateFlow(
        mapOf(
            "Album" to 0,
            "Single" to 0,
            "Compilation" to 0,
            "EP" to 0
        )
    )

    val albumType = _albumTypes.map { it["Album"] ?: 0 }.stateIn(
        viewModelScope, SharingStarted.Eagerly, 0
    )

    val compilationType = _albumTypes.map { it["Compilation"] ?: 0 }.stateIn(
        viewModelScope, SharingStarted.Eagerly, 0
    )

    val singleType = _albumTypes.map { it["Single"] ?: 0 }.stateIn(
        viewModelScope, SharingStarted.Eagerly, 0
    )

    val epType = _albumTypes.map { it["EP"] ?: 0 }.stateIn(
        viewModelScope, SharingStarted.Eagerly, 0
    )

    fun getAlbumById(id: String) = viewModelScope.launch {
        _detail.value = UiState.Loading

        getRemoteAlbumUseCase.getAlbumById(id)
            .catch { e -> _detail.value = UiState.Error(e) }
            .collect { result -> _detail.value = UiState.Success(result) }
    }

    fun getNewReleases() {
        if (isPaginationEnded) {
            _newRelease.value = UiState.Success(emptyList())
            return
        }

        viewModelScope.launch {
            _newRelease.value = UiState.Loading

            getRemoteAlbumUseCase.getNewReleases(offset = offset, limit = PAGE_SIZE)
                .catch { e -> _newRelease.value = UiState.Error(e) }
                .collect { albums ->
                    handlePaginatedResult(albums, _newRelease)
                }
        }
    }

    fun searchAlbums(query: String) {
        if (isPaginationEnded) {
            _search.value = UiState.Success(emptyList())
            return
        }

        viewModelScope.launch {
            _search.value = UiState.Loading

            getRemoteAlbumUseCase.searchAlbums(query = query, offset = offset, limit = PAGE_SIZE)
                .catch { e -> _search.value = UiState.Error(e) }
                .collect { albums ->
                    handlePaginatedResult(albums, _search)
                }
        }
    }

    fun resetSearch() {
        offset = 0
        isPaginationEnded = false
        _search.value = UiState.Success(emptyList())
    }

    fun searchAlbumByName(query: String) {
        viewModelScope.launch(dispatcherIO) {
            _searchResult.emit(emptyList())
            val albums = getLocalAlbumUseCase.searchAlbumByName(query)
            loadAlbumsWithAdaptiveChunks(albums).collectLatest { _searchResult.emit(it) }
        }
    }

    fun searchAlbumsListenLater(query: String) {
        viewModelScope.launch(dispatcherIO) {
            _searchResult.emit(emptyList())
            val albums = getLocalAlbumUseCase.searchAlbumsListenLater(query)
            loadAlbumsWithAdaptiveChunks(albums).collectLatest { _searchResult.emit(it) }
        }
    }

    fun getAlbumType(albumType: String) = loadAlbums {
        getLocalAlbumUseCase.getAlbumsByType(albumType)
    }

    fun getAlbumWithMostTracks() {
        viewModelScope.launch {
            statisticsAlbumUseCase.getAlbumWithMostTracks().collect { album ->
                _albumWithMostTracks.value = album
            }
        }
    }

    fun getMostPopularDecade() {
        viewModelScope.launch {
            statisticsAlbumUseCase.getMostPopularDecade().collect { decade ->
                _decadeResult.value = decade
            }
        }
    }

    fun getListenLaterCount() {
        viewModelScope.launch {
            statisticsAlbumUseCase.getListenLaterCount().collect { count ->
                _listenLaterCount.value = count
            }
        }
    }

    fun getRandomAlbum() {
        viewModelScope.launch {
            getLocalAlbumUseCase.getRandomAlbum().collect { random ->
                _randomAlbum.value = random
            }
        }
    }

    fun getAlbumCount() {
        viewModelScope.launch {
            statisticsAlbumUseCase.getAlbumCount().collect { count ->
                _albumCount.value = count
            }
        }
    }

    fun getTotalTracksCount()  {
        viewModelScope.launch {
            statisticsAlbumUseCase.getTotalTracksCount().collect { count ->
                _totalTracksCount.value = count
            }
        }
    }

    fun getAlbumTypeCount(albumType: String) {
        viewModelScope.launch {
            val count = statisticsAlbumUseCase.getAlbumTypeCount(albumType).firstOrNull() ?: 0
            _albumTypes.update { currentTypes ->
                currentTypes.toMutableMap().apply {
                    put(albumType, count)
                }
            }
        }
    }

    suspend fun getStarsById(albumId: String) : List<StarsEntity> = withContext(dispatcherIO) {
        getStarsUseCase(GetStarsUseCase.Params.GetStars(albumId)) as List<StarsEntity>
    }

    suspend fun insertStars(star : List<StarsEntity>) = withContext(dispatcherIO) {
        getStarsUseCase(GetStarsUseCase.Params.InsertStars(star))
    }

    suspend fun deleteStars(albumId: String) = withContext(dispatcherIO) {
        getStarsUseCase(GetStarsUseCase.Params.DeleteStars(albumId))
    }

    fun getSavedAlbums() = loadAlbums {
        getLocalAlbumUseCase.getSavedAlbums()
    }

    fun getSavedAlbumAsc() = loadAlbums {
        getLocalAlbumUseCase.getSavedAlbumAsc()
    }

    fun getAlbumSortedByName() = loadAlbums {
        getLocalAlbumUseCase.getAlbumSortedByName()
    }

    fun getListenLaterAlbums() = loadAlbums {
        getLocalAlbumUseCase.getListenLaterAlbums()
    }

    suspend fun insertAlbum(album: AlbumEntity) = withContext(dispatcherIO) {
        saveAlbumUseCase(SaveAlbumUseCase.Params.SaveAlbum(album))
    }

    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) = withContext(dispatcherIO) {
        saveAlbumUseCase(SaveAlbumUseCase.Params.MarkAsSaved(isAlbumSaved))
    }

    suspend fun insertListenLaterAlbum(isListenLaterSaved: IsListenLaterSaved) = withContext(dispatcherIO) {
        saveAlbumUseCase(SaveAlbumUseCase.Params.AddToListenLater(isListenLaterSaved))
    }

    suspend fun getAlbum(albumId: String): AlbumEntity? = withContext(dispatcherIO) {
        getAlbumUseCase(GetAlbumUseCase.Params.GetAlbum(albumId)) as? AlbumEntity?
    }

    suspend fun getSavedAlbumState(albumId: String): IsAlbumSaved? = withContext(dispatcherIO) {
        getAlbumUseCase(GetAlbumUseCase.Params.GetSavedAlbumState(albumId)) as? IsAlbumSaved?
    }

    suspend fun getListenLaterState(albumId: String): IsListenLaterSaved? = withContext(dispatcherIO) {
        getAlbumUseCase(GetAlbumUseCase.Params.GetListenLaterState(albumId)) as? IsListenLaterSaved?
    }

    suspend fun getAlbumWithStates(albumId: String): AlbumWithStates? = withContext(dispatcherIO) {
        getAlbumUseCase(GetAlbumUseCase.Params.GetAlbumWithStates(albumId)) as? AlbumWithStates?
    }

    suspend fun deleteAlbum(albumId: String) = withContext(dispatcherIO) {
        deleteAlbumUseCase(DeleteAlbumUseCase.Params.DeleteAlbum(albumId))
    }

    suspend fun deleteSavedAlbumState(albumId: String) = withContext(dispatcherIO) {
        deleteAlbumUseCase(DeleteAlbumUseCase.Params.RemoveFromSaved(albumId))
    }

    suspend fun deleteListenLaterState(albumId: String) = withContext(dispatcherIO) {
        deleteAlbumUseCase(DeleteAlbumUseCase.Params.RemoveFromListenLater(albumId))
    }

    private fun loadAlbumsWithAdaptiveChunks(
        albums : List<AlbumEntity>
    ) : Flow<List<AlbumEntity>> = flow {
        val displayedAlbums = mutableListOf<AlbumEntity>()
        val config = getCacheConfig()

        for (chunk in albums.chunked(config.chunkSize)) {
            displayedAlbums.addAll(chunk)
            emit(displayedAlbums.toList())
            delay(config.delayTime)
        }
    }

    private fun getCacheConfig() : CacheConfig {
        val runtime = Runtime.getRuntime()
        val availableProcessors = runtime.availableProcessors()
        val freeMemory = runtime.freeMemory() / (1024 * 1024)

        return when {
            availableProcessors >= 8 && freeMemory > 4000 -> CacheConfig(20, 500L)
            availableProcessors >= 4 && freeMemory > 2000 -> CacheConfig(15, 750L)
            else -> CacheConfig(10, 1000L)
        }
    }

    private fun <T> handlePaginatedResult(
        newItems : List<T>,
        stateFlow : MutableStateFlow<UiState<List<T>>>
    ) {
        if (newItems.isEmpty()) {
            isPaginationEnded = true
        } else {
            val currentList = (stateFlow.value as? UiState.Success)?.data ?: emptyList()
            val updatedList = currentList + newItems
            stateFlow.value = UiState.Success(updatedList)
            offset += PAGE_SIZE
            isPaginationEnded = newItems.size < PAGE_SIZE
        }
    }

    private fun loadAlbums(fetch : suspend () -> List<AlbumEntity>) {
        viewModelScope.launch(dispatcherIO) {
            val albums = fetch()
            loadAlbumsWithAdaptiveChunks(albums).collectLatest { _albumEntity.emit(it) }
        }
    }
}