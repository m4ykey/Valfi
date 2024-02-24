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
import com.m4ykey.data.local.model.AlbumEntity
import com.m4ykey.data.local.model.ListenLaterEntity
import com.m4ykey.ui.helpers.ListSortingType
import com.m4ykey.ui.helpers.ViewType
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumSearchUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private var _search = MutableLiveData<AlbumSearchUiState>()
    val albums : LiveData<AlbumSearchUiState> get() = _search

    private var _detail = MutableLiveData<AlbumDetailUiState>()
    val detail : LiveData<AlbumDetailUiState> get() = _detail

    private var _tracks = MutableLiveData<AlbumTrackUiState>()
    val tracks : LiveData<AlbumTrackUiState> get() = _tracks

    private var _albumPagingData = MutableLiveData<PagingData<AlbumEntity>>()
    val albumPagingData: LiveData<PagingData<AlbumEntity>> = _albumPagingData

    private var _listenLaterPagingData = MutableLiveData<PagingData<ListenLaterEntity>>()
    val listenLaterPagingData: LiveData<PagingData<ListenLaterEntity>> = _listenLaterPagingData

    private var _currentViewType = MutableLiveData(ViewType.GRID)
    val currentViewType : LiveData<ViewType> = _currentViewType

    private var _localAlbum = MutableLiveData<AlbumEntity>()
    val localAlbum : LiveData<AlbumEntity> get() = _localAlbum

    private var _listenLaterAlbum = MutableLiveData<ListenLaterEntity>()
    val listenLaterAlbum : LiveData<ListenLaterEntity> get() = _listenLaterAlbum

    private var currentSortingType : ListSortingType = ListSortingType.RECENTLY_ADDED

    private var _searchResult = MutableLiveData<Flow<PagingData<AlbumEntity>>>()
    val searchResult : LiveData<Flow<PagingData<AlbumEntity>>> = _searchResult

    init {
        getAllAlbumsPaged()
        getListenLaterAlbums()
    }

    private fun getListenLaterAlbums() {
        viewModelScope.launch {
            repository.getListenLaterAlbums().cachedIn(viewModelScope).collect { pagingData ->
                _listenLaterPagingData.value = pagingData
            }
        }
    }

    fun getListenLaterCount() : Flow<Int> = repository.getListenLaterCount()

    fun searchAlbumsByName(searchQuery : String) {
        _searchResult.value = repository.searchAlbumsByName(searchQuery)
    }

    fun getAlbumsOfTypeCompilationPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeCompilationPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAlbumsOfTypeAlbumPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeAlbumPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAlbumsOfTypeEPPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeEPPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAlbumsOfTypeSinglePaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeSinglePaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.postValue(pagingData)
            }
        }
    }

    fun getAllAlbumsPaged() {
        viewModelScope.launch {
            when (currentSortingType) {
                ListSortingType.ALPHABETICAL -> {
                    repository.getAlbumSortedAlphabetical().cachedIn(viewModelScope).collect { pagingData ->
                        _albumPagingData.postValue(pagingData)
                    }
                }
                ListSortingType.RECENTLY_ADDED -> {
                    repository.getAllAlbumsPaged().cachedIn(viewModelScope).collect { pagingData ->
                        _albumPagingData.postValue(pagingData)
                    }
                }
            }
        }
    }

    fun updateSortingType(sortingType: ListSortingType) {
        viewModelScope.launch {
            currentSortingType = sortingType
            getAllAlbumsPaged()
        }
    }

    suspend fun getLocalAlbumById(albumId : String) {
        withContext(Dispatchers.IO) {
            val album = repository.getLocalAlbumById(albumId)
            _localAlbum.postValue(album)
        }
    }

    suspend fun getListenLaterAlbum(albumId : String) {
        withContext(Dispatchers.IO) {
            val album = repository.getListenLaterAlbumById(albumId)
            _listenLaterAlbum.postValue(album)
        }
    }

    suspend fun saveAlbum(album : AlbumEntity) {
        viewModelScope.launch { repository.saveAlbum(album) }
    }

    suspend fun deleteAlbum(album: AlbumEntity) {
        viewModelScope.launch { repository.deleteAlbum(album) }
    }

    suspend fun saveListenLater(album : ListenLaterEntity) {
        viewModelScope.launch { repository.saveListenLater(album) }
    }

    suspend fun deleteListenLater(album : ListenLaterEntity) {
        viewModelScope.launch { repository.deleteListenLater(album) }
    }

    suspend fun getAlbumById(id : String) {
        repository.getAlbumById(id).onEach { result ->
            _detail.value = handleDetailState(result)
        }.launchIn(viewModelScope)
    }

    fun searchAlbums(query : String) {
        viewModelScope.launch {
            _search.value = AlbumSearchUiState(isLoading = true)
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
        _search.value = AlbumSearchUiState(error = e.message)
    }

    private fun handleTrackError(e : Exception) {
        _tracks.value = AlbumTrackUiState(error = e.message)
    }

    private fun handleSearchResult(result : Flow<Resource<PagingData<AlbumItem>>>) {
        viewModelScope.launch {
            result.collect { resource ->
                resource.handleResult(
                    onSuccess = { albums ->
                        _search.value = AlbumSearchUiState(albumSearch = albums)
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
}