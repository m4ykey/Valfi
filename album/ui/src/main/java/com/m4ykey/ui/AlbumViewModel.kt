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
import com.m4ykey.ui.adapter.AlbumEntityPagingAdapter
import com.m4ykey.ui.helpers.ListSortingType
import com.m4ykey.ui.uistate.AlbumDetailUiState
import com.m4ykey.ui.uistate.AlbumSearchUiState
import com.m4ykey.ui.uistate.AlbumTrackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var _albumPagingData = MutableLiveData<PagingData<AlbumEntity>>()
    val albumPagingData: LiveData<PagingData<AlbumEntity>> = _albumPagingData

    private var _currentViewType = MutableLiveData(AlbumEntityPagingAdapter.ViewType.GRID)
    val currentViewType : LiveData<AlbumEntityPagingAdapter.ViewType> = _currentViewType

    private var _localAlbum = MutableLiveData<AlbumEntity>()
    val localAlbum : LiveData<AlbumEntity> get() = _localAlbum

    private var currentSortingType : ListSortingType = ListSortingType.RECENTLY_ADDED

    init {
        getAllAlbumsPaged()
    }

    fun getAlbumsOfTypeCompilationPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeCompilationPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.value = pagingData
            }
        }
    }

    fun getAlbumsOfTypeAlbumPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeAlbumPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.value = pagingData
            }
        }
    }

    fun getAlbumsOfTypeEPPaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeEPPaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.value = pagingData
            }
        }
    }

    fun getAlbumsOfTypeSinglePaged() {
        viewModelScope.launch {
            repository.getAlbumsOfTypeSinglePaged().cachedIn(viewModelScope).collect { pagingData ->
                _albumPagingData.value = pagingData
            }
        }
    }

    fun getAllAlbumsPaged() {
        viewModelScope.launch {
            when (currentSortingType) {
                ListSortingType.ALPHABETICAL -> {
                    repository.getAlbumSortedAlphabetical().cachedIn(viewModelScope).collect { pagingData ->
                        _albumPagingData.value = pagingData
                    }
                }
                ListSortingType.RECENTLY_ADDED -> {
                    repository.getAllAlbumsPaged().cachedIn(viewModelScope).collect { pagingData ->
                        _albumPagingData.value = pagingData
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

    suspend fun insertAlbum(album : AlbumEntity) {
        viewModelScope.launch { repository.insertAlbum(album) }
    }

    suspend fun deleteAlbum(album : AlbumEntity) {
        viewModelScope.launch { repository.deleteAlbum(album) }
    }

    suspend fun getAlbumById(id : String) {
        repository.getAlbumById(id).onEach { result ->
            when (result) {
                is Resource.Loading -> { _detail.value = AlbumDetailUiState(true, null, null) }
                is Resource.Success -> { _detail.value = AlbumDetailUiState(false, null, result.data) }
                is Resource.Error -> { _detail.value = AlbumDetailUiState(false, result.message ?: "Unknown message", null) }
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